package com.builditcreative.newsapp.api

class EverythingRequest private constructor(builder: Builder) {
    val q: String
    val sources: String
    val domains: String
    val from: String
    val to: String
    val language: String
    val sortBy: String
    val pageSize: String
    val page: String

    init {
        q = builder.q.toString()
        sources = builder.sources.toString()
        domains = builder.domains.toString()
        from = builder.from.toString()
        to = builder.to.toString()
        language = builder.language.toString()
        sortBy = builder.sortBy.toString()
        pageSize = builder.pageSize.toString()
        page = builder.page.toString()
    }

    class Builder {
        var q: String? = null
        var sources: String? = null
        var domains: String? = null
        var from: String? = null
        var to: String? = null
        var language: String? = null
        var sortBy: String? = null
        var pageSize: String? = null
        var page: String? = null
        fun q(q: String?): Builder {
            this.q = q
            return this
        }

        fun sources(sources: String?): Builder {
            this.sources = sources
            return this
        }

        fun domains(domains: String?): Builder {
            this.domains = domains
            return this
        }

        fun from(from: String?): Builder {
            this.from = from
            return this
        }

        fun to(to: String?): Builder {
            this.to = to
            return this
        }

        fun language(language: String?): Builder {
            this.language = language
            return this
        }

        fun sortBy(sortBy: String?): Builder {
            this.sortBy = sortBy
            return this
        }

        fun pageSize(pageSize: Int): Builder {
            this.pageSize = pageSize.toString()
            return this
        }

        fun page(page: Int): Builder {
            this.page = page.toString()
            return this
        }

        fun build(): EverythingRequest {
            return EverythingRequest(this)
        }
    }
}