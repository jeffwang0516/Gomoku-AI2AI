
class Triplet<L, M, R> {
	L x;
	M y;
	R n;

	Triplet(L x, M y, R n) {
		this.x = x;
		this.y = y;
		this.n = n;
	}

	L getX() {
		return x;
	}

	M getY() {
		return y;
	}

	R getN() {
		return n;
	}
}
