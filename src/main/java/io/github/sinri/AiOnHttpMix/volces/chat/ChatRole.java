package io.github.sinri.AiOnHttpMix.volces.chat;

/**
 * 发出该消息的对话参与者的角色，可选 system, user 或 assistant。
 * role 参数的设定有助于模型理解对话的结构和上下文，从而生成更准确、更合适的回应。
 * 例如，在多轮对话中，role可以帮助模型追踪不同参与者的发言和意图，即使在复杂的对话场景中也能保持连贯性。
 */
public enum ChatRole {
    system, user, assistant
}
