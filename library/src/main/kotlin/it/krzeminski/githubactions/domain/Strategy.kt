package it.krzeminski.githubactions.domain

public open class Strategy<MATRIX : Matrix>(
    public val matrix: MATRIX,
) {
    public object EMPTY : Strategy<Matrix.EMPTY>(Matrix.EMPTY)
}
