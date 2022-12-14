package dev.nestorperez.registrybackend.mutations

import com.expediagroup.graphql.server.operations.Query
import dev.nestorperez.registrybackend.model.AppVersion
import dev.nestorperez.registrybackend.util.parseAppVersion
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Controller

@Controller
class AppVersionQuery : Query {
    @Autowired
    lateinit var env: Environment
    fun appVersion(): AppVersion? = parseAppVersion(env)
}
