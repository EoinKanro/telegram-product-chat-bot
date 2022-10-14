package io.github.eoinkanro.telegram.info.chat.bot.core.conf;

import io.github.eoinkanro.telegram.info.chat.bot.core.model.data.ConfigPaths;
import io.github.eoinkanro.telegram.info.chat.bot.core.utils.FileUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.doccat.*;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.*;
import opennlp.tools.util.model.ModelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Slf4j
@Component
public class OpenNLPConf {

    @Value("${opennlp.language:en}")
    private String language;

    @Getter
    @Value("${opennlp.notFoundAnswer:Answer for your request not found}")
    private String notFoundAnswer;

    @Autowired
    private FileUtils fileUtils;

    @Getter
    private boolean initialized;

    private SentenceDetectorME sentenceDetector;
    private TokenizerME tokenizer;
    private POSTaggerME posTagger;
    private DictionaryLemmatizer lemmatizer;
    private DocumentCategorizerME categorizer;

    @PostConstruct
    private void init() {
        try {
            initSentenceDetector();
            initTokenizer();
            initPosTagger();
            initLemmatizer();
            initCategorizer();
            initialized = true;
        } catch (Exception e) {
            log.warn("OpenNLP is not configured...", e);
        }
    }

    private void initSentenceDetector() throws IOException {
        try (InputStream modelIn = Files.newInputStream(fileUtils.getOpenNlpFile(ConfigPaths.SENTENCE_DICT.getPath()).toPath())) {
            sentenceDetector = new SentenceDetectorME(new SentenceModel(modelIn));
        }
    }

    private void initTokenizer() throws IOException {
        try (InputStream modelIn = Files.newInputStream(fileUtils.getOpenNlpFile(ConfigPaths.TOKENIZER_DICT.getPath()).toPath())) {
            tokenizer = new TokenizerME(new TokenizerModel(modelIn));
        }
    }

    private void initPosTagger() throws IOException {
        try (InputStream modelIn = Files.newInputStream(fileUtils.getOpenNlpFile(ConfigPaths.POSTAGGER_DICT.getPath()).toPath())) {
            posTagger = new POSTaggerME(new POSModel(modelIn));
        }
    }

    private void initLemmatizer() throws IOException {
        try (InputStream modelIn = Files.newInputStream(fileUtils.getOpenNlpFile(ConfigPaths.LEMMATIZER_DICT.getPath()).toPath())) {
            lemmatizer = new DictionaryLemmatizer(modelIn);
        }
    }

    private void initCategorizer() throws IOException {
        InputStreamFactory inputStreamFactory = new MarkableFileInputStreamFactory(fileUtils.getOpenNlpFile(ConfigPaths.CATEGORIZER_DICT.getPath()));
        ObjectStream<String> lineStream = new PlainTextByLineStream(inputStreamFactory, StandardCharsets.UTF_8);
        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

        DoccatFactory factory = new DoccatFactory(new FeatureGenerator[] {new BagOfWordsFeatureGenerator()});

        TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
        params.put(TrainingParameters.CUTOFF_PARAM, 0);

        DoccatModel doccatModel = DocumentCategorizerME.train(language, sampleStream, params, factory);
        categorizer = new DocumentCategorizerME(doccatModel);
    }

    @Nullable
    public String getCategory(String data) {
        String[] sentences = sentenceDetector.sentDetect(data);

        for (String sentence : sentences) {
            String[] tokens = tokenizer.tokenize(sentence);
            String[] posTags = posTagger.tag(tokens);
            String[] lemmas = lemmatizer.lemmatize(tokens, posTags);
            double[] probabilitiesOfOutcomes = categorizer.categorize(lemmas);
            String category = categorizer.getBestCategory(probabilitiesOfOutcomes);

            if (category != null && !category.isEmpty()) {
                return category;
            }
        }
        return null;
    }
}
