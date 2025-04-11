package io.github.sinri.AiOnHttpMix.mirage.vl;

import io.github.sinri.AiOnHttpMix.mix.vl.AnyVLLMResponse;
import io.github.sinri.keel.core.json.UnmodifiableJsonifiableEntityImpl;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.NotNull;

public class MirageSyncVLResponse extends UnmodifiableJsonifiableEntityImpl {
    public MirageSyncVLResponse(@NotNull JsonObject jsonObject) {
        super(jsonObject);
    }

    public String getRequestId() {
        return readString("request_id");
    }

    public JsonObject getData() {
        return readJsonObject("data");
    }

    /*
    {
        "request_id":"10.8.85.217-141946-fc266802-fcd5-4c79-8ace-e43db39518ec",
        "code":"OK",
        "data":{
            "role":"assistant",
            "content":[
                {
                    "text":"抱歉，我无法提供关于图片中旗帜的信息。但是从视觉内容来看，在普京总统的身后有一面俄罗斯国旗和一面克里姆林宫旗。\n\n- 俄罗斯联邦国旗：通常被称为“白蓝红三色旗”，自苏联解体后一直作为俄罗斯的国家象征。\n  \n- 克里姆林宫旗（也称为莫斯科或斯拉夫扬卡旗）：这是一面红色底、白色边框的三角形旗帜，中间有一个蓝色的小十字架在中心位置，并且有金色的边缘装饰。它代表了位于莫斯科市中心的历史建筑群——克里姆林宫。\n\n请注意，这些信息是基于图像的内容以及对相关历史背景的理解提供的。如果您需要更详细或者准确的信息，请告诉我！"
                }
            ]
        }
     }
     */

    public AnyVLLMResponse toAnyVLLMResponse() {
        JsonObject data = getData();
        //        System.out.println("MirageSyncVLResponse::toAnyVLLMResponse with data: "+data);
        return AnyVLLMResponse.fromMirageVLResponseData(data);
    }
}
