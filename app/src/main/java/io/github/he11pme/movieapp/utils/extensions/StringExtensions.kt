package io.github.he11pme.movieapp.utils.extensions

fun String.toValidPath(): String {
    return this.replace(" ", "_").replace(Regex("[^a-zA-Zа-яА-Я0-9_]"), "")
}