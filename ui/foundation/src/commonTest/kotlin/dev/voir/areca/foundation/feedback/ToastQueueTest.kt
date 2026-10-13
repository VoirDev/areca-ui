package dev.voir.areca.foundation.feedback

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class ToastQueueTest {
    private val first = ToastData(label = "first")
    private val second = ToastData(label = "second")
    private val third = ToastData(label = "third")

    @Test
    fun `toasts are shown one at a time in the order they were queued`() {
        val queue = ToastQueue()
        queue.enqueue(first)
        queue.enqueue(second)
        queue.enqueue(third)

        val shown = buildList {
            while (true) {
                val current = queue.current.value ?: break
                add(current)
                queue.dismissCurrent()
            }
        }

        assertEquals(listOf(first, second, third), shown)
    }

    @Test
    fun `dismissing a waiting toast skips it and keeps the current one`() {
        val queue = ToastQueue()
        queue.enqueue(first)
        queue.enqueue(second)
        queue.enqueue(third)

        queue.dismiss(second.id)

        assertEquals(first, queue.current.value)
        queue.dismissCurrent()
        assertEquals(third, queue.current.value)
    }

    @Test
    fun `dismissing a toast that already left does not close its successor`() {
        val queue = ToastQueue()
        queue.enqueue(first)
        queue.enqueue(second)
        queue.dismiss(first.id)

        queue.dismiss(first.id)

        assertEquals(second, queue.current.value)
    }

    @Test
    fun `clearing drops the current toast and every waiting one`() {
        val queue = ToastQueue()
        queue.enqueue(first)
        queue.enqueue(second)

        queue.clear()
        queue.enqueue(third)

        assertEquals(third, queue.current.value)
        queue.dismissCurrent()
        assertNull(queue.current.value)
    }

    @Test
    fun `enqueueing returns the id that dismisses the toast`() {
        val queue = ToastQueue()
        val firstId = queue.enqueue(first)
        val secondId = queue.enqueue(second)

        queue.dismiss(secondId)
        queue.dismiss(firstId)

        assertEquals(first.id, firstId)
        assertNotEquals(firstId, secondId)
        assertNull(queue.current.value)
    }
}
