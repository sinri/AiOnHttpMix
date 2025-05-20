package io.github.sinri.AiOnHttpMix.utils;

public interface StreamPieceCollector<P, E> {
    void accept(P piece);

    E build();
}
