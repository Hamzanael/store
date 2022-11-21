package com.bloobloom.assessment.service

import com.bloobloom.assessment.api.v1.request.UpdateFrameRequest
import com.bloobloom.assessment.integration.currency.CurrencyConverter
import com.bloobloom.assessment.model.Currency
import com.bloobloom.assessment.model.Frame
import com.bloobloom.assessment.model.Price
import com.bloobloom.assessment.model.Status
import com.bloobloom.assessment.model.repository.FrameRepository
import com.bloobloom.assessment.service.exceptions.frame.NoFramesInStockAvailable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FrameService {
    @Autowired
    private lateinit var frameRepository: FrameRepository

    @Autowired
    private lateinit var currencyConverter: CurrencyConverter

    fun create(frame: Frame): Frame = frameRepository.insert(frame)

    fun getFrames(): List<Frame> {
        val allFrames = frameRepository.findAll()
        calculateFramesPricesInMultiCurrency(allFrames)
        return allFrames
    }

    fun getFramesByStatus(status: Status): List<Frame> {
        val allFrames = frameRepository.findAllByStatus(status)
        calculateFramesPricesInMultiCurrency(allFrames)
        return allFrames
    }

    private fun calculateFramesPricesInMultiCurrency(allFrames: MutableList<Frame>) {
        allFrames.forEach { frame ->
            frame.prices = Currency.values().map { currency ->
                Price(
                    currency = currency,
                    value = currencyConverter.convert(frame.price.value, currency)
                )
            }.toSet()
        }
    }

    fun update(id: String, frameRequest: UpdateFrameRequest): Frame {
        val frame = frameRepository.findById(id).orElseThrow { Exception("Frame not found") }
        val newFrame = frame.copy(
            name = frameRequest.name ?: frame.name,
            description = frameRequest.description ?: frame.description,
            price = frameRequest.price ?: frame.price,
            stock = frameRequest.stock ?: frame.stock,
            status = frameRequest.status ?: frame.status
        )
        newFrame.id = frame.id
        return frameRepository.save(newFrame)
    }


    fun getFrameIfAvailable(id: String): Frame {
        val frame = frameRepository.findById(id).orElseThrow { Exception("Frame not found") }
        if (frame.stock == 0) {
            throw NoFramesInStockAvailable("frame with id $id is not in stock")
        }
        return frame
    }


    fun buyFrame(id: String, quantity: Int): Frame {
        val frame = frameRepository.findById(id).orElseThrow { Exception("Frame not found") }
        logger.debug("Buying frame with id $id")
        if (frame.stock < quantity) {
            logger.error("Not enough stock for frame with id $id")
            throw NoFramesInStockAvailable("Not enough stock")
        }
        logger.debug("Frame with id $id has enough stock")
        val newFrame = frame.copy(stock = frame.stock - quantity)
        newFrame.id = frame.id
        return frameRepository.save(newFrame)
    }

    fun addFrameToStock(id: String, quantity: Int): Frame {
        val frame = frameRepository.findById(id).orElseThrow { Exception("Frame not found") }
        val newFrame = frame.copy(stock = frame.stock + quantity)
        newFrame.id = frame.id
        return frameRepository.save(newFrame)
    }

    fun delete(id: String) = frameRepository.deleteById(id)


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(FrameService::class.java)
    }
}
