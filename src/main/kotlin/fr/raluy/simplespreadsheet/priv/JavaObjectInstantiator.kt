package fr.raluy.simplespreadsheet.priv

import java.lang.reflect.Constructor

class JavaObjectInstantiator<T : Any> : AbstractObjectInstantiator<Class<T>, Constructor<*>, T>() {

    override fun findCtorWithCorrectNbOfargs(jCLass: Class<T>, size: Int): Constructor<*>? =
        jCLass.constructors
            .filter { it.parameters.size == size }.firstOrNull()

    override fun getDefaultCtor(clazz: Class<T>): Constructor<*>? =
        clazz.constructors.filter { it.parameters.isEmpty() }.firstOrNull()


    override fun getAnyCtor(clazz: Class<T>): Constructor<*>? =
        clazz.constructors.firstOrNull()

    override fun createObject(clazz: Class<T>, ctorParams: Array<String?>): T {
        val constructor = getCtor(clazz, ctorParams);

        // trim/add the array of params so that they match the nb of operands of the ctor
        val params = ctorParams.copyOf(constructor.parameters.size)

        val obj = constructor.newInstance(* params) as T

        if (constructor.parameters.isEmpty()) {
            // IF ctor is the default one, fill the params manually
            setFields(obj, ctorParams)
        }

        return obj
    }

}