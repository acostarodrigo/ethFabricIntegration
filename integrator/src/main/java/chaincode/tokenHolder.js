module.exports = class TokenHolder {
        constructor(address) {
        this._address = address
    }

    get address() {
        return this._address;
    }
}