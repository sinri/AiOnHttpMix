# Contribution Guide for LLM

This document would guide you how to contribute for LLM related codes.

## Concept

This project, as an SDK, is to provide an encapsulation of certain LLM abilities through the APIs provided by the service providers.

When we use LLM, we have to determine which **model** to use. 
Here, the **model** would be `gpt-4o` (of OpenAI ChatGPT series) or `QwenPlus` (of Dashscope Qwen series). 
Commonly, different models are with different ability and style, along with different pricing, which affects the decision.
The models are defined as interface `io.github.sinri.AiOnHttpMix.utils.models.ChatModel`.

In this project, we use HTTP API to use the models.
Some of the models are designed with same API format, that means they can share one Request-and-Response implementation; 
meanwhile, others are not shareable.
For the models share one API format, even one endpoint, we defined a **model series** to group them.
The model series are defined as `io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification`.

A service provider would provide service for a series of LLMs through various APIs.
Such as Azure (Microsoft), Dashscope (Aliyun), Volces (ByteDance), etc., each of them provides many LLM services.
The service providers are defined as interface `io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider`. 

The above is the mapping entities defined for the LLM services in the real world.
When we use APIs to reach a certain LLM service, we need to follow the direction of each provider for its model series.
To implement that, we abstract out a **Model Service** entity,
and the interface `io.github.sinri.AiOnHttpMix.utils.ChatModelServiceAdapter` for executing.

## Implement a certain LLM service

### Step 1: ensure the service provider

Define a class implementing interface `io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider` in package `io.github.sinri.AiOnHttpMix.utils.providers`;
this class should be public, and its constructor should be package-protected;
this class should define a static final String field named as `NAME` for the service provider name, and return it in method `getName`;
finally, set a const field as the unique instance of this class in interface `io.github.sinri.AiOnHttpMix.utils.providers.ServiceProvider`.

### Step 2: ensure the model series

Like step 1;
define a class implementing interface `io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification` in package `io.github.sinri.AiOnHttpMix.utils.specification`.
finally, set a const field as the unique instance of this class in interface `io.github.sinri.AiOnHttpMix.utils.specification.ModelSpecification`.

### Step 3: ensure the model

Like step 1:
define a class implementing interface `io.github.sinri.AiOnHttpMix.utils.models.ChatModel` in package `io.github.sinri.AiOnHttpMix.utils.models`.
finally, set a const field as the unique instance of this class in interface `io.github.sinri.AiOnHttpMix.utils.models.ChatModel`.

### Step 4: build the model service 

As a model service is for one model series, it would be implemented in package `io.github.sinri.AiOnHttpMix.provider.SERVICE_PROVIDER.MODEL_SERVICE`, where `SERVICE_PROVIDER` and `MODEL_SERVICE` should be replaced.