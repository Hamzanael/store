package com.bloobloom.assessment.service

import com.bloobloom.assessment.constants.Constants.NUMBER_OF_ALLOWED_QUANTITY
import com.bloobloom.assessment.constants.ConstantsTest.EMPTY_PRICE
import com.bloobloom.assessment.constants.ConstantsTest.NUMBER_TEN
import com.bloobloom.assessment.constants.ConstantsTest.TEST_FRAME
import com.bloobloom.assessment.constants.ConstantsTest.TEST_UPDATE_FRAME_REQUEST
import com.bloobloom.assessment.integration.currency.CurrencyConverter
import com.bloobloom.assessment.model.Currency.USD
import com.bloobloom.assessment.model.Status
import com.bloobloom.assessment.model.repository.FrameRepository
import com.bloobloom.assessment.service.exceptions.frame.NoFramesInStockAvailable
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import java.util.Optional
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
internal class FrameServiceTest {
    @MockK
    private lateinit var frameRepository: FrameRepository

    @MockK
    private lateinit var currencyConverter: CurrencyConverter

    @InjectMockKs
    private lateinit var frameService: FrameService

    @Test
    fun `should create frame with the giving frame object`() {
        every {
            frameRepository.insert(
                TEST_FRAME
            )
        } answers {
            val insertedFrame = TEST_FRAME.copy()
            insertedFrame.id = "1"
            insertedFrame
        }
        val frame = frameService.create(
            TEST_FRAME
        )
        assertThat(frame).isEqualTo(TEST_FRAME)
    }

    @Test
    fun `should get all frames with currency conversion`() {
        every { frameRepository.findAll() } returns mutableListOf(TEST_FRAME)
        every { currencyConverter.convert(any(), any()) } returns NUMBER_TEN
        val frames = frameService.getFrames()
        assertThat(frames[0].prices.first().currency).isEqualTo(USD)
        assertThat(frames).isEqualTo(listOf(TEST_FRAME))
    }

    @Test
    fun `should get all active frames only`() {
        every { frameRepository.findAllByStatus(Status.ACTIVE) } returns mutableListOf(TEST_FRAME)
        every { currencyConverter.convert(any(), any()) } returns NUMBER_TEN
        val frames = frameService.getFramesByStatus(Status.ACTIVE)
        assertThat(frames).hasSize(1)
        assertThat(frames[0].prices.first().currency).isEqualTo(USD)
        assertThat(frames).isEqualTo(listOf(TEST_FRAME))
    }

    @Test
    fun `should update frame based on the request`() {
        val updatedFrame = TEST_FRAME.copy(
            name = "Updated Name",
            description = "Updated Description",
            price = EMPTY_PRICE,
            stock = 10,
            status = Status.ACTIVE
        )
        TEST_FRAME.id = "1"
        every { frameRepository.findById(any()) } returns Optional.of(TEST_FRAME)
        every { frameRepository.save(any()) } answers {
            updatedFrame.id = "1"
            updatedFrame
        }

        val frame = frameService.update("1", TEST_UPDATE_FRAME_REQUEST)
        assertThat(frame).isEqualTo(updatedFrame)
    }

    @Test
    fun `should get frame only if stock is greater than zero`() {
        TEST_FRAME.id = "1"
        every { frameRepository.findById(any()) } returns Optional.of(TEST_FRAME)
        val frame = frameService.getFrameIfAvailable("1")
        assertThat(frame).isEqualTo(TEST_FRAME)
    }

    @Test
    fun `should throw an exception if stock is less than zero`() {
        TEST_FRAME.id = "1"
        every { frameRepository.findById(any()) } returns Optional.of(TEST_FRAME.copy(stock = 0))
        assertThatThrownBy { frameService.getFrameIfAvailable("1") }.isInstanceOf(NoFramesInStockAvailable::class.java)
    }


    @Test
    fun `should be able to buy frame if there is items in the stock and the quantity is the default quantity`() {
        TEST_FRAME.id = "1"
        every { frameRepository.findById(any()) } returns Optional.of(TEST_FRAME)
        every { frameRepository.save(any()) } answers {
            TEST_FRAME.copy(stock = TEST_FRAME.stock - NUMBER_OF_ALLOWED_QUANTITY)
        }
        val frame = frameService.buyFrame("1", NUMBER_OF_ALLOWED_QUANTITY)
        assertThat(frame.stock).isEqualTo(TEST_FRAME.stock - NUMBER_OF_ALLOWED_QUANTITY)
    }

    @Test
    fun `should not be able to buy frame if there is no items in the stock`() {
        TEST_FRAME.id = "1"
        every { frameRepository.findById(any()) } returns Optional.of(TEST_FRAME.copy(stock = 0))
        assertThatThrownBy { frameService.buyFrame("1", NUMBER_OF_ALLOWED_QUANTITY) }.isInstanceOf(
            NoFramesInStockAvailable::class.java
        )
    }

    @Test
    fun addFrameToStock() {
        TEST_FRAME.id = "1"
        every { frameRepository.findById(any()) } returns Optional.of(TEST_FRAME)
        every { frameRepository.save(any()) } answers {
            TEST_FRAME.copy(stock = TEST_FRAME.stock + NUMBER_OF_ALLOWED_QUANTITY)
        }
        val frame = frameService.addFrameToStock("1", NUMBER_OF_ALLOWED_QUANTITY)
        assertThat(frame.stock).isEqualTo(TEST_FRAME.stock + NUMBER_OF_ALLOWED_QUANTITY)
    }

    @Test
    fun delete() {
        TEST_FRAME.id = "1"
        every { frameRepository.deleteById(any()) } answers {}
        frameService.delete("1")
        verify(atLeast = 1) { frameRepository.deleteById(any()) }
    }


}