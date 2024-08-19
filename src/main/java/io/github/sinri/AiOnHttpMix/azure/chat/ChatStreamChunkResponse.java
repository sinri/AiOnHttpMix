package io.github.sinri.AiOnHttpMix.azure.chat;

import io.github.sinri.keel.core.json.SimpleJsonifiableEntity;
import io.vertx.core.json.JsonObject;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 突然重现不了这种返回格式了。
 */
@Deprecated
public class ChatStreamChunkResponse extends SimpleJsonifiableEntity {
    public static final String ObjectCode = "chat.completion.chunk";
    // DEBUG|data:{
    //  "id": "224dc65f-b48e-476e-b926-c1b844ec5db4",
    //  "model": "gpt-4",
    //  "created": 1702536912,
    //  "object": "chat.completion.chunk",
    //  "choices": [
    //      {
    //          "index": 0,
    //          "messages": [
    //              {
    //                  "delta": {
    //                      "role": "tool",
    //                      "content": "{\"citations\": [{\"content\": \"com/#/homePage，使用钉钉扫码即可登录，或通过OC——乐其产品小站——EP进入控制台（如果无功能权限，请联系运营工厂进行开通）。\\r\\n2、创建任务\\r\\nStep1：进入商品申报质检界面\\r\\n点击“质检”下的“天猫大促质检”→“商品申报质检”可进入到该应用；\\r\\n\\r\\nStep2：创建商品申报质检任务\\r\\n进入商品申报质检界面，点击“新增质检任务”可新建一个立刻执行的商品申报质检任务；PS：若在统筹质检界面中已经创建并执行完成一个商品申报质检任务,可在此界面筛选并查看风险项；\\r\\n\\r\\n填写想新建商品申报质检任务的信息，点击“保存”即可创建新的立刻执行的商品申报质检任务；PS：当存在待执行与执行中的同一活动同一店铺的质检任务与此质检活动不在质检节奏里时，无法进行创建；\\r\\n\\r\\n字段说明：\\r\\n  字段  说明  备注\\r\\n  风险内容  即参加此次活动店铺选品的商品ID、SKUI  有风险情况：\\r\\n  D、活动价是否存在风险；  1、若EP选品表中A商品只存在商品ID而\\r\\n  天猫的A商品既有商品ID又有SKUID，则\\r\\n  生成风险项“选品表缺少SKU”；\\r\\n  2、若EP选品表中A商品存在商品ID和S\\r\\n  KUID而天猫的A商品只存在商品ID，则生\", \"id\": null, \"title\": \"商品申报质检\", \"filepath\": \"商品申报质检.txt\", \"url\": \"https://seventhtowerfield.blob.core.windows.net/fileupload-qc/%E5%95%86%E5%93%81%E7%94%B3%E6%8A%A5%E8%B4%A8%E6%A3%80.txt\", \"metadata\": {\"chunking\": \"orignal document size=1005. Scores=8.6744175Org Highlight count=76.\"}, \"chunk_id\": \"0\"}, {\"content\": \"点击“任务列表”可进入展示全部质检任务的任务中心，找到想要查看的质检任务，点击“查看质检结果”或“导出”可查看全部的质检项（有风险+无风险的商品）；\\r\\n4、处理风险结果\\r\\n进入“零售价预警/一口价预警”界面，勾选需要确认风险的商品，点击确认风险结果，完成风险项确认；PS：1、点击确认风险后如果不去天猫后台/OMS系统进行相应的修改，下一次的质检会检查出同样的风险，请及时进行风险修改；2、点击确认无风险，则表示该条风险信息为合理异常，不需要进行任何修改。（建议将商品添加至质检白名单，则下次质检不会继续报风险）\\r\\n5、设置零售价质检&一口价质检商品白名单\\r\\n如果有特殊商品无需质检零售价或一口价，可以申请对应的商品白名单，申请通过后，将不再对此商品进行零售价或一口价的质检；\\r\\nStep1：进入申请质检白名单界面\\r\\n点击该功能的【设置质检规则】按钮，选择【零售价预警剔除/一口价预警剔除】可进入到该应用；\\r\\n点击“新建商品剔除”，填写基本信息，点击新增发起申请；\", \"id\": null, \"title\": \"品牌价格质检\", \"filepath\": \"品牌价格质检.txt\", \"url\": \"https://seventhtowerfield.blob.core.windows.net/fileupload-qc/%E5%93%81%E7%89%8C%E4%BB%B7%E6%A0%BC%E8%B4%A8%E6%A3%80.txt\", \"metadata\": {\"chunking\": \"orignal document size=962. Scores=5.139506Org Highlight count=49.\"}, \"chunk_id\": \"0\"}, {\"content\": \"直播秒杀质检\\r\\n基本介绍\\r\\n直播秒杀是一档在直播过程中放置秒杀商品来增加用户量的活动，在配置秒杀活动时，如果商品防拍价格、秒杀价格、秒杀库存、单人限购数量等出现设置错误可能会造成资损，因此EP可以帮助店铺检查以上属性是否正确设置，规避运营风险。\\r\\n二、质检逻辑\\r\\n直播秒杀质检每天定时质检店铺淘宝直播秒杀商品防拍价、单人限购数量、秒杀库存是否满足设置的质检规则。\\r\\n以下情况会生成风险项：\\r\\n（1）若不维护质检规则，则在质检任务中后台所有的商品全部触发有风险；\\r\\n（2）商品为【虚拟商品】，但未在EP中开启“防拍价质检过滤虚拟商品”，且天猫后台防拍价低于EP质检规则中设置的防拍价；\\r\\n（3）商品为【非虚拟商品】且天猫后台防拍价低于EP质检规则中设置的防拍价；\\r\\n（4）商品为【虚拟商品】，但未在EP中开启“秒杀库存质检过滤虚拟商品”，且天猫后台设置的秒杀库存大于EP质检规则中设置的秒杀库存；\\r\\n（5）商品为【非虚拟商品】，天猫后台设置的秒杀库存大于EP质检规则中设置的秒杀库存；\", \"id\": null, \"title\": \"直播秒杀质检\", \"filepath\": \"直播秒杀质检.txt\", \"url\": \"https://seventhtowerfield.blob.core.windows.net/fileupload-qc/%E7%9B%B4%E6%92%AD%E7%A7%92%E6%9D%80%E8%B4%A8%E6%A3%80.txt\", \"metadata\": {\"chunking\": \"orignal document size=986. Scores=5.256997Org Highlight count=58.\"}, \"chunk_id\": \"0\"}, {\"content\": \"商品广告法\\r\\n基本介绍\\r\\n【抖音日常质检-商品广告法质检】功能主要检测店铺商品的手机端/PC端主图、商品标题、类目属性、详情图这些模块中是否存在业务配置的风险违禁词。\\r\\n二、质检逻辑\\r\\n商品的手机端/PC端主图、类目属性、详情图、商品标题这些模块中在剔除配置的过滤关键词后，如果还存在配置的违禁词时，则报有风险。\\r\\n三、如何使用（操作流程）\\r\\n1、登录EP控制台\\r\\n在浏览器中输入网址：https://ep.leqee\", \"id\": null, \"title\": \"商品广告法\", \"filepath\": \"商品广告法.txt\", \"url\": \"https://seventhtowerfield.blob.core.windows.net/fileupload-qc/%E5%95%86%E5%93%81%E5%B9%BF%E5%91%8A%E6%B3%95.txt\", \"metadata\": {\"chunking\": \"orignal document size=405. Scores=6.849376Org Highlight count=23.\"}, \"chunk_id\": \"0\"}, {\"content\": \"com/#/homePage，使用钉钉扫码即可登录，或通过OC——乐其产品小站——EP进入控制台（如果无功能权限，请联系运营工厂进行开通）。\\r\\n2、配置店铺宝质检规则\\r\\nStep1：进入店铺宝质检\\r\\n点击【质检】下的【天猫日常质检】→【店铺宝质检】可进入到该应用；\\r\\n\\r\\nStep2：配置质检规则\\r\\n点击“设置质检规则”下的【店铺宝预警配置】进入质检规则配置界面；\\r\\n\\r\\n根据店铺的质检需求，配置相应的质检红线；\\r\\nPS：1、“店铺赠品预警数量”为最低数量，即赠品的上架库存低于设置的值时触发有风险。此项如果不设置的话，则不进行该项的质检；\\r\\n2、店铺宝赠品处于下架状态时触发有风险；\\r\\n3、天猫后台店铺宝折扣率设置的“店铺宝预警折扣率”时，触发有风险。当店铺宝设置多级时，如有任意一级低于设置同样触发风险。此项如果不设置的话，则不进行该项的质检；\\r\\n4、天猫后台设置上不封顶规则与“店铺上不封顶规则”设置不一样时触发风险；\\r\\n\\r\\nStep3：剔除不参与质检的店铺宝\", \"id\": null, \"title\": \"店铺宝质检\", \"filepath\": \"店铺宝质检.txt\", \"url\": \"https://seventhtowerfield.blob.core.windows.net/fileupload-qc/%E5%BA%97%E9%93%BA%E5%AE%9D%E8%B4%A8%E6%A3%80.txt\", \"metadata\": {\"chunking\": \"orignal document size=881. Scores=4.8145204Org Highlight count=46.\"}, \"chunk_id\": \"0\"}], \"intent\": \"[\\\"商品质检功能介绍\\\", \\\"商品质量检查功能说明\\\", \\\"如何进行商品质量检验\\\"]\"}"
    //                  },
    //                  "index": 0,
    //                  "end_turn": false
    //              }
    //          ],
    //          "finish_reason": null
    //      }
    //  ]
    // }
    public ChatStreamChunkResponse(JsonObject jsonObject) {
        super(jsonObject);
    }

    public String getId() {
        return readString("id");
    }

    public String getModel() {
        return readString("model");
    }

    public Long getCreated() {
        return readLong("created");
    }

    public String getObject() {
        return readString("object");
    }

    public List<Choice> getChoices() {
        List<Choice> list = new ArrayList<>();

        List<JsonObject> array = readJsonObjectArray("choices");

        if (array != null) {
            array.forEach(x -> {
                list.add(new Choice(x));
            });
        }
        return list;
    }

    public static class Choice extends SimpleJsonifiableEntity {
        public Choice(JsonObject jsonObject) {
            super(jsonObject);
        }

        public Long getIndex() {
            return readLong("index");
        }

        public List<Message> getMessages() {
            List<Message> list = new ArrayList<>();

            List<JsonObject> array = readJsonObjectArray("messages");

            if (array != null) {
                array.forEach(x -> {
                    list.add(new Message(x));
                });
            }
            return list;
        }

        public Delta getDelta() {
            return new Delta(readJsonObject("delta"));
        }

        public String getFinishReason() {
            return readString("finish_reason");
        }
    }

    public static class Message extends SimpleJsonifiableEntity {
        public Message(JsonObject jsonObject) {
            super(jsonObject);
        }

        public Long getIndex() {
            return readLong("index");
        }

        public Boolean getEndTurn() {
            return readBoolean("end_turn");
        }

        public Delta getDelta() {
            return new Delta(readJsonObject("delta"));
        }
    }

    public static class Delta extends SimpleJsonifiableEntity {
        public Delta(JsonObject jsonObject) {
            super(jsonObject);
        }

        public String getRole() {
            return readString("role");
        }

        public @Nullable String getContent() {
            return readString("content");
        }
    }
}
