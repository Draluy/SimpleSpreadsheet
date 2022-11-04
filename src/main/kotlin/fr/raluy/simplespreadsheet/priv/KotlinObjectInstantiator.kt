package fr.raluy.simplespreadsheet.priv

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility

class KotlinObjectInstantiator<T : Any> : AbstractObjectInstantiator<KClass<T>, KFunction<T>, T>() {

    override fun findCtorWithCorrectNbOfargs(clazz: KClass<T>, size: Int): KFunction<T>? =
        clazz.constructors
            .filter { it.visibility == KVisibility.PUBLIC && it.parameters.size == size }.firstOrNull()

    override fun getDefaultCtor(clazz: KClass<T>): KFunction<T>? =
        clazz.constructors.filter { it.visibility == KVisibility.PUBLIC && it.parameters.isEmpty() }.firstOrNull()

    override fun getAnyCtor(clazz: KClass<T>): KFunction<T>? =
        clazz.constructors.filter { it.visibility == KVisibility.PUBLIC }.firstOrNull()

    override fun createObject(clazz: KClass<T>, ctorParams: Array<String?>): T {
        val constructor = getCtor(clazz, ctorParams);

        // trim/add the array of params so that they match the nb of operands of the ctor
        val params = ctorParams.copyOf(constructor.parameters.size)

        val obj = constructor.call(* params)

        if (constructor.parameters.isEmpty()) {
            // IF ctor is the default one, fill the params manually
            setFields(obj, ctorParams)
        }

        return obj
    }
}