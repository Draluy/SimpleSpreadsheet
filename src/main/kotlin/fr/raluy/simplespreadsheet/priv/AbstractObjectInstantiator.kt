package fr.raluy.simplespreadsheet.priv

import fr.raluy.simplespreadsheet.reader.SSException

abstract class AbstractObjectInstantiator<CLASS, RETURN, OBJECT_TYPE : Any> {
    abstract fun findCtorWithCorrectNbOfargs(clazz: CLASS, size: Int): RETURN?

    abstract fun getDefaultCtor(clazz: CLASS): RETURN?

    abstract fun getAnyCtor(clazz: CLASS): RETURN?

    protected fun setFields(obj: OBJECT_TYPE, ctorParams: Array<String?>) {
        val memberProperties =
            obj.javaClass.declaredFields // USe java fields, to read them in the order of declaration
        var index = 0
        memberProperties
            .forEach {
                if (index < ctorParams.size) {
                    it.trySetAccessible()
                    it.set(obj, ctorParams[index])
                }
                index++
            }
    }

    fun getCtor(clazz: CLASS, ctorParams: Array<String?>): RETURN {
        return findCtorWithCorrectNbOfargs(clazz, ctorParams.size)
            ?: getDefaultCtor(clazz)
            ?: getAnyCtor(clazz)
            ?: throw SSException("No suitable constructor found for class $clazz. Check that it has at least a public constructor.")
    }

    abstract fun createObject(clazz: CLASS, ctorParams: Array<String?>): OBJECT_TYPE
}