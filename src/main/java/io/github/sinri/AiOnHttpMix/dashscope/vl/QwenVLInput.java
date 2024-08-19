package io.github.sinri.AiOnHttpMix.dashscope.vl;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;


public class QwenVLInput extends SimpleJsonifiableEntity {

    public QwenVLInput(JsonObject jsonObject) {
        super(jsonObject);
    }

    public QwenVLInput() {
        this(new JsonObject().put("messages", new JsonArray()));
    }


//    public QwenVLParameter setMessages(@Nonnull List<Message> messages) {
//        var array = new JsonArray();
//        messages.forEach(message -> {
//            array.add(message.toJsonObject());
//        });
//        this.jsonObject.put("messages", array);
//        return this;
//    }

    public QwenVLInput addMessage(@NotNull QwenVLMessage message) {
        this.jsonObject.getJsonArray("messages").add(message.toJsonObject());
        return this;
    }

}
