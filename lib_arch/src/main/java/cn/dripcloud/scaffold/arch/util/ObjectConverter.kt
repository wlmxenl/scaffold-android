package cn.dripcloud.scaffold.arch.util

/**
 * 对象转换
 * @author wangzf
 * @date 2022/3/28
 */
interface ObjectConverter<in T, out R> {
    fun convert(t: T): R
}