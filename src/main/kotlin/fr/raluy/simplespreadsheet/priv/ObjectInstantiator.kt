package fr.raluy.simplespreadsheet.priv

import fr.raluy.simplespreadsheet.reader.SSException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility

class ObjectInstantiator {

    companion object {
        private fun <T : Any> findCtorWithCorrectNbOfargs(kClass: KClass<T>, size: Int): KFunction<T>? =
            kClass.constructors
                .filter { it.visibility == KVisibility.PUBLIC && it.parameters.size == size }.firstOrNull()

        private fun <T : Any> getDefaultCtor(kClass: KClass<T>): KFunction<T>? =
            kClass.constructors.filter { it.visibility == KVisibility.PUBLIC && it.parameters.isEmpty() }.firstOrNull()

        private fun <T : Any> getAnyCtor(kClass: KClass<T>): KFunction<T>? =
            kClass.constructors.filter { it.visibility == KVisibility.PUBLIC }.firstOrNull()

        fun <T : Any> createObject(kClass: KClass<T>, ctorParams: Array<String?>): T {
            val constructor =
                findCtorWithCorrectNbOfargs(kClass, ctorParams.size)
                    ?: getDefaultCtor(kClass)
                    ?: getAnyCtor(kClass)
                    ?: throw SSException("No suitable constructor found for class $kClass. Check that it has at least a public constructor.")

            // trim/add the array of params so that they match the nb of operands of the ctor
            val params = ctorParams.copyOf(constructor.parameters.size)

            return constructor.call(* params)
        }
    }
}