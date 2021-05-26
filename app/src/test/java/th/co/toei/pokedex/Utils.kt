package th.co.toei.pokedex

import org.mockito.Mockito
import kotlin.reflect.KClass

inline fun <reified T : Any> anyType() = Mockito.any(T::class.java) ?: createInstance(T::class)

fun <T : Any> createInstance(kClass: KClass<T>): T = castNull()

@Suppress("UNCHECKED_CAST")
private fun <T> castNull(): T = null as T