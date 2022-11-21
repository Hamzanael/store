package com.bloobloom.assessment.integration.fixerAPI

import com.bloobloom.assessment.constants.Constants.CURRENCY_CACHE_KEY
import com.bloobloom.assessment.integration.currency.CurrencyAPIResponse
import com.bloobloom.assessment.integration.currency.CurrencyConverter
import com.bloobloom.assessment.model.Currency
import com.bloobloom.assessment.model.Currency.*
import com.bloobloom.assessment.service.ICacheService
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "fixer.api")
@Component
class FixerAPI : CurrencyConverter {

    @Autowired
    private lateinit var httpClient: HttpClient

    @Autowired
    private lateinit var cacheService: ICacheService<FixerAPIResponse>

    lateinit var endpoint: String

    lateinit var apiKey: String

    lateinit var baseCurrency: String


    override fun getCurrencyRates(): CurrencyAPIResponse {
        if (cacheService.get(CURRENCY_CACHE_KEY) != null) {
            return cacheService.get(CURRENCY_CACHE_KEY) as FixerAPIResponse
        }
        try {
            val fixerAPIResponse = runBlocking<FixerAPIResponse> {
                httpClient.get {
                    url {
                        url(endpoint)
                        parameters.append("symbols", buildCurrencyQuery())
                        parameters.append("base", baseCurrency)
                    }
                    headers {
                        append("Content-Type", "application/json")
                        append("apiKey", apiKey)
                    }
                }.body()
            }
            cacheService.set(CURRENCY_CACHE_KEY, fixerAPIResponse)
        } catch (redirect: RedirectResponseException) {
            logger.error("Redirected to ${redirect.response.status.value} ${redirect.response.status.description}")
            return fallbackAPIResponse()
        } catch (clientRequestException: ClientRequestException) {
            logger.error("Client request exception: ${clientRequestException.message}")
            return fallbackAPIResponse()
        } catch (serverResponseException: ServerResponseException) {
            logger.error("Server response exception: ${serverResponseException.message}")
            return fallbackAPIResponse()
        } catch (timeoutException: HttpRequestTimeoutException) {
            logger.error("Timeout exception: ${timeoutException.message}")
            return fallbackAPIResponse()
        } catch (noTransformationFoundException: NoTransformationFoundException) {
            logger.error("No transformation found exception: ${noTransformationFoundException.message}")
            return fallbackAPIResponse()
        } catch (exception: Exception) {
            logger.error("Exception: ${exception.message}")
            return fallbackAPIResponse()
        }
        throw FixerAPIException("Fixer API returned null")
    }

    override fun convert(value: Double, currency: Currency): Double {
        val rates = getCurrencyRates().rates
        val rate = rates[currency] ?: throw Exception("Currency not found")
        return value * rate
    }

    private fun buildCurrencyQuery(): String {
        val currencies = Currency.values().map { it.name }
        return currencies.joinToString(",")
    }

    private fun fallbackAPIResponse(): FixerAPIResponse {
        val fallBackResponse = FixerAPIResponse(
            success = false,
            timestamp = 1668776522,
            base = "USD",
            date = "2022-11-18",
            rates = mapOf(
                EUR to 0.965325,
                USD to 1.0,
                GBP to 0.84068,
                JOD to 0.708981,
                JPY to 140.026501,
            )
        )
        cacheService.set(CURRENCY_CACHE_KEY, fallBackResponse)
        return fallBackResponse
    }


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(FixerAPI::class.java)
    }
}