package pt.dourobats.app.features.login.api.usecase

import pt.dourobats.app.core.common.Result

interface RequestLoginCodeUseCase {
    suspend operator fun invoke(email: String): Result<Unit>
}
