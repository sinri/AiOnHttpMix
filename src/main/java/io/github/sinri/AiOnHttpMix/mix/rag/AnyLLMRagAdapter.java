package io.github.sinri.AiOnHttpMix.mix.rag;

import io.github.sinri.AiOnHttpMix.mix.AnyLLMSimpleRoleMessagePair;
import io.vertx.core.Future;

import java.util.List;


/**
 * The {@code AnyLLMRagAdapter} interface provides methods to search for references from RAG (Retrieval-Augmented
 * Generation) based on the provided message or a list of message pairs.
 * <p>
 * This interface is designed to be implemented by adapters that interact with RAG systems, allowing for asynchronous
 * retrieval of reference text.
 *
 * @since 1.2.6
 */
public interface AnyLLMRagAdapter {


    /**
     * Asynchronously searches for references from RAG (Retrieval-Augmented Generation) based on the provided message.
     *
     * @param message the input message to search for references
     * @return a {@link Future} that will be completed with the reference text from RAG related to the input message
     */
    Future<String> searchReferencesFromRAG(String message);

    /**
     * Searches for references from RAG (Retrieval-Augmented Generation) based on the last message in the provided list
     * of message pairs.
     *
     * @param messagePairList a list of {@link AnyLLMSimpleRoleMessagePair} objects, where each pair represents a role
     *                        and its corresponding message
     * @return a {@link Future} that will be completed with the reference text from RAG related to the input message
     */
    default Future<String> searchReferencesFromRAG(List<AnyLLMSimpleRoleMessagePair> messagePairList) {
        AnyLLMSimpleRoleMessagePair lastMessage = messagePairList.get(messagePairList.size() - 1);
        String message = lastMessage.message();
        return searchReferencesFromRAG(message);
    }
}
