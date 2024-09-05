# AI on HTTP Mix

Work with online LLM services of Azure OpenAI ChatGPT, Aliyun Qwen, and Volces.

## Prepare

You should prepare authentication for the LLM service,
with which you can create the instances of

* `AzureOpenAIServiceMeta` in package `io.github.sinri.AiOnHttpMix.azure.openai.core`
* `DashscopeServiceMeta` in package `io.github.sinri.AiOnHttpMix.dashscope.core`
* `VolcesServiceMeta` in package `io.github.sinri.AiOnHttpMix.volces.core`

to perform API call.

## Usage

Simply, you can use the static methods of `io.github.sinri.AiOnHttpMix.AigcMix` to get response entity from certain LLM
service.