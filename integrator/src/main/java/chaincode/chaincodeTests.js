const TokenHolder = require('./tokenHolder');
const TokenWallet = require('./tokenWallet');


let holder = new TokenHolder("address");
let wallet = new TokenWallet(holder,10);

var car = {
    docType: 'car',
    make: "citroen",
    model: "aircross",
    color: "grey",
    owner: {name: "rodrigo",
        nick: "chino"}
};

var tokenWallet = {_holder: {_address:"address"}, _balance: 10};

console.log(JSON.stringify(wallet));
console.log(JSON.stringify(tokenWallet));
console.log(JSON.stringify(car));
