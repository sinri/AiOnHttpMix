# Contribution Guide for LLM

This document guides you on how to contribute LLM-related code to this project.

## Concept

This project, as an SDK, encapsulates certain LLM capabilities via APIs provided by service providers.

When using LLMs, you must determine which **model** to use. For example, a **model** could be
`gpt-4o` (from the OpenAI ChatGPT series) or
`qwen-plus` (from the Dashscope Qwen series). Different models have different abilities, styles, and pricing, which affect your choice. Models are defined by the interface
`io.github.sinri.AiOnHttpMix.utils.models.ChatModel`.

All LLMs in this project are accessed via HTTP APIs. Some models share the same API format and can use the same request/response implementation; others cannot. Models sharing the same API format (and often the same endpoint) are grouped into a
**model series**, defined by `io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification`.

A service provider (e.g., Azure, Dashscope, Volces) offers a set of LLMs via various APIs. Service providers are defined by the interface
`io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider`.

To abstract the real-world LLM service mapping, this project introduces the concept of a **Model Service
** and the interface `io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter` for execution.

## How to Implement a New LLM Service

### Step 1: Ensure the Service Provider

- Implement the `ServiceProvider` interface in the package `io.github.sinri.AiOnHttpMix.utils.providers` (e.g.,
  `AzureOpenAIServiceProvider`, `DashscopeServiceProvider`, `VolcesServiceProvider`).
- The constructor should be package-protected (no modifier).
- The singleton instance should be exposed as a static field in the `ServiceProvider` interface (e.g.,
  `ServiceProvider.azureOpenAI`).
- Implement the `getProviderName()` method to return the unique provider name.

### Step 2: Ensure the Model Series

- Implement the `ModelSpecification` interface as an abstract class in the package
  `io.github.sinri.AiOnHttpMix.utils.specification` (e.g., `DashscopeModelSpecification`, `GPTModelSpecification`).
- Provide a static name field (e.g., `SPECIFICATION_NAME`).
- Implement the `buildServiceAdapter(KeelConfigElement config)` method to return the corresponding
  `ChatModelServiceAdapter`.

### Step 3: Ensure the Model

- Implement the `ChatModel` interface as an abstract class in the package
  `io.github.sinri.AiOnHttpMix.utils.models` or its subpackages (e.g., `QwenModelSeries`, `GPTModelSeries`).
- Provide a static factory method (e.g., `model(String modelName)`) to obtain a concrete model instance.
- Implement the `getModelName()` method to return the model name.

### Step 4: Build the Model Service

- For each model series, implement a service adapter (e.g., `QwenServiceAdapter`,
  `OpenAIServiceAdapter`) that implements the `ChatModelServiceAdapter` interface. Place it in the package
  `io.github.sinri.AiOnHttpMix.provider.SERVICE_PROVIDER.MODEL_SERVICE`, replacing `SERVICE_PROVIDER` and
  `MODEL_SERVICE` accordingly.
- The service adapter should be obtained via the model series'
  `buildServiceAdapter` method, using configuration parameters as needed.

---

#### Example: Dashscope Qwen

- **Service Provider**: `io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider.dashscope`
- **Model Series**: `io.github.sinri.AiOnHttpMix.utils.models.dashscope.qwen.QwenModelSeries`
- **Model**: `QwenModelSeries.model("qwen-plus")`
- **Service Adapter**: `io.github.sinri.AiOnHttpMix.provider.dashscope.qwen.QwenServiceAdapter`

---

#### Key Points

1. **Singleton Access
   **: Service providers and model series are exposed as static fields in their respective interfaces, not as public static final fields in the implementation class.
2. **Model Instantiation**: Models are obtained via static factory methods (e.g.,
   `model(String modelName)`) in the abstract class, not as separate classes for each model.
3. **Service Adapter Construction**: Use the model series'
   `buildServiceAdapter` method with configuration parameters to obtain the adapter, rather than direct instantiation.

For further code examples or documentation updates, please refer to the actual implementation in the
`io.github.sinri.AiOnHttpMix.utils` package and its subpackages.