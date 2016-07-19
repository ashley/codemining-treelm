package codemining.lm.tsg.tui.java;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang.exception.ExceptionUtils;

import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.StructureNode;
import codemining.ast.TreeNode;
import codemining.ast.distilledchanges.ChangeDistillerTreeExtractor;
import codemining.ast.java.AbstractJavaTreeExtractor;
import codemining.lm.tsg.FormattedTSGrammar;
import codemining.lm.tsg.TSGNode;
import codemining.lm.tsg.samplers.AbstractTSGSampler;
import codemining.lm.tsg.samplers.CollapsedGibbsSampler;
import codemining.util.serialization.Serializer;
import codemining.util.serialization.ISerializationStrategy.SerializationException;

public class CDSampleTSG {
	private static final Logger LOGGER = Logger.getLogger(CDSampleTSG.class.getName());

	public static void main(final String[] args) throws IOException,
	SerializationException {
		if (args.length != 3) { 
			System.err
			.println("Usage <TrainingDir1> <TrainingDir2> <#iterations>");
			return;
		} 

		final int nIterations = Integer.parseInt(args[2]);
		final File samplerCheckpoint = new File("tsgSampler.ser");

		final String serializedFile = 
				args.length == 3 ? args[2].trim() + ".ser" : "CDtsg.ser";
		
		final CollapsedGibbsSampler sampler;

		if (samplerCheckpoint.exists()) {
			sampler = (CollapsedGibbsSampler) Serializer.getSerializer()
					.deserializeFrom("tsgSampler.ser");
			LOGGER.info("Resuming sampling");
		} else {
			final ChangeDistillerTreeExtractor format = new ChangeDistillerTreeExtractor();
			sampler = new CollapsedGibbsSampler(20, 10,
					new FormattedTSGrammar(format),
					new FormattedTSGrammar(format));

			File trainingDir1 = new File(args[0]);

			Collection<File> files1 = FileUtils.listFiles(
					trainingDir1, 
					new RegexFileFilter(".*java"), 
					DirectoryFileFilter.DIRECTORY
					);


			final double percentRootsInit = .9;
			int nFiles = 0;
			int nNodes = 0;

			FileDistiller distiller = ChangeDistiller.createFileDistiller(Language.JAVA);


			for(File f1 : files1) {
				String path1 = f1.getPath();
				System.err.println("args1: " + args[0]);
				String[] split = path1.split(args[0]);
				String relativePath = split[1];
				System.err.println("path1: " + path1 + " relative path: " + relativePath + " path2: " + args[1] + "/" + relativePath);
				File f2 = new File(args[1] + "/" + relativePath);
				if(f2.exists()) { 
					StructureNode outcome = distiller.extractClassifiedSourceCodeChanges(f1, f2);
					List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
					for (SourceCodeChange change: changes){
						System.out.print(change);

						final TreeNode<TSGNode> changeTree = TSGNode.convertTree(format.getTree(change), percentRootsInit);
								nNodes += changeTree.getTreeSize();
						nFiles++;
						sampler.addTree(changeTree);

					}
				}
			}

			final AtomicBoolean finished = new AtomicBoolean(false);
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					int i = 0;
					while (!finished.get() && i < 1000) {
						try {
							Thread.sleep(500);
							i++;
						} catch (final InterruptedException e) {
							LOGGER.warning(ExceptionUtils.getFullStackTrace(e));
						}
					}
				}

			});

			final int nItererationCompleted = sampler.performSampling(nIterations);

			final FormattedTSGrammar grammarToUse;
			if (nItererationCompleted >= nIterations) {
				LOGGER.info("Sampling complete. Outputing burnin grammar...");
				grammarToUse = (FormattedTSGrammar) sampler.getBurnInGrammar();
			} else {
				LOGGER.warning("Sampling not complete. Outputing sample grammar...");
				grammarToUse = (FormattedTSGrammar) sampler.getSampleGrammar();
			}
			try {
				Serializer.getSerializer().serialize(grammarToUse, serializedFile);
			} catch (final Throwable e) {
				LOGGER.severe("Failed to serialize grammar: "
						+ ExceptionUtils.getFullStackTrace(e));
			}

			try {
				Serializer.getSerializer().serialize(sampler,
						"tsgSamplerCheckpoint.ser");
			} catch (final Throwable e) {
				LOGGER.severe("Failed to checkpoint sampler: "
						+ ExceptionUtils.getFullStackTrace(e));
			}

			// sampler.pruneNonSurprisingRules(1);
			sampler.pruneRareTrees((int) (AbstractTSGSampler.BURN_IN_PCT * nIterations) - 10);
			System.out.println(grammarToUse.toString());
			finished.set(true); // we have finished and thus the shutdown hook can
			// now stop waiting for us.

		}
	}
}
