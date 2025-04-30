package io.github.sinri.AiOnHttpMix.test.unit.provider.dashscope.qwen;

import io.github.sinri.AiOnHttpMix.dashscope.qwen.text.request.QwenRequest;
import io.vertx.core.Future;
import org.junit.Test;

import java.util.UUID;

public class QwenKitMTUnitTest extends AbstractQwenKitUnitTest {

    @Test
    public void testTranslateJP2CN() {
        String jp =
                """
                何とも素早い行動だが、やはり知らない犬と一緒にされるのはストレスだったのか、しばらく甘え癖が出た。
                アーデルハイトは入れて早々、シェパードに対して激しい攻撃性を見せたことで失敗となる。
                ケーニッヒは非常に淡泊だった。即座に状況を理解したのかシェパードと交尾をする。しかし終われば早く出せと言わんばかりにほえたため、静子は慌てて彼を開放した。
                カイザーもケーニッヒ同様に、理解力は高かったがこちらはもっと深刻だった。しばらく静子の元を離れず、彼女が寝ているときもそばにいるほど甘え癖が出た。
                リッターと違いカイザーは巨体ゆえ少々骨が折れたが、群れが崩壊しなかったことを考えれば安いものと思い、しばらくカイザーたちの甘え癖に付き合った。
                これで３匹の遺伝子が受け継がれ、後は産まれた子犬の中から優良個体を選別し、さらに柴犬などと交配していくことで、優れたジャーマン・シェパード・ウルフドックが誕生する。
                彼らの子が、孫が、後の世で警備犬として活躍することを静子は願った。
                
                「雄のオウギワシを頼んでいるけど、すぐに輸送されるとは思えないしね。アカとクロは勝手に連れてくると思うけど、うちで巣作りしそうな予感がする」
                
                報告書を読んだ静子は頭が痛かった。オウギワシはつがいを生涯変えない猛禽類ゆえに、雄のオウギワシを日本へ輸送してもらうようフロイスへ依頼した。
                しかしオウギワシの捕獲は困難で、さらにシロガネが雄を気に入るか未知数だった。アカガネとクロガネに至っては品種不明だ。２羽には自力でつがいを見つけてくれる他ない。
                
                「静子様、伴天連から例のものが届きました」
                
                「お！ ついに着ましたか！ 早速、温泉室（>・・・>）に例のものを運んでおいて！」
                
                読んでいた報告書をその辺に投げ捨てると、静子は意気揚々と自室へ戻る。ため息を吐いた後、彩は静子が投げ捨てた報告書を拾った。
                
                
                「案外腐るの早いんだね、カカオって」
                
                開いた果実の大半が腐っていたことに、静子は分かっていながらもため息を吐く。
                
                静子がフロイスに頼んだ植物、それは昨年依頼したコーヒーやカカオの苗木や種だ。
                日本では栽培が不可能に思われがちなカカオだが、実は伊豆でカカオの栽培を行っている農園はいくつかある。
                熱帯地域で育つカカオをどのような方法で栽培しているか、その秘密が温泉から出る排湯だ。
                
                温泉から出る湯は大半が使われず捨てられている。静子の温泉も例に漏れず、大半が利用されず川へ流されていた。
                """;

        // "周囲に凄惨（>せいさん>）な僧兵の死体がなければ、戦場映画のように映える。だが、残念なことに彼らの周りは血と臓物が飛び散っていた。";
        async(() -> {
            return getKit().chatForMessageResponse(
                                   getServiceMeta(),
                                   req -> {
                                       req.setModel("qwen-mt-turbo");
                                       req.handleInput(input -> input
                                               .addUserMessage(jp));
                                       req.handleParameters(p -> {
                                           p.handleTranslationOptions(translationOptions -> translationOptions
                                                   .setSourceLang(QwenRequest.Parameters.TranslationOptions.Language.JAPANESE)
                                                   .setTargetLang(QwenRequest.Parameters.TranslationOptions.Language.CHINESE)
                                                   .setDomains("Translate in Light Novel Style.")
                                           );
                                       });
                                   },
                                   UUID.randomUUID().toString()
                           )
                           .compose(resp -> {
                               getUnitTestLogger().info("resp", resp.cloneAsJsonObject());
                               return Future.succeededFuture();
                           });
        });
    }
}
