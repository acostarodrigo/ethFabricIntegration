pragma solidity ^0.4.0;

contract ETHBridge {
    /// Number of authorities signatures required to withdraw the money.
    ///
    /// Must be lesser than number of authorities.
    uint256 public requiredSignatures;

    /// The gas cost of calling `HomeBridge.withdraw`.
    ///
    /// Is subtracted from `value` on withdraw.
    /// recipient pays the relaying authority for withdraw.
    /// this shuts down attacks that exhaust authorities funds on home chain.
    uint256 public estimatedGasCostOfWithdraw;

    /// reject deposits that would increase `this.balance` beyond this value.
    /// security feature:
    /// limits the total amount of home/mainnet ether that can be lost
    /// if the bridge is faulty or compromised in any way!
    /// set to 0 to disable.
    uint256 public maxTotalHomeContractBalance;

    /// reject deposits whose `msg.value` is higher than this value.
    /// security feature.
    /// set to 0 to disable.
    uint256 public maxSingleDepositValue;

    /// Contract authorities.
    address[] public authorities;

    /// Used foreign transaction hashes.
    //mapping (bytes32 => bool) withdraws;

    /// Event created on money deposit.
    event Deposit (address recipient, uint256 value);

    /// Event created on money withdraw.
    event Withdraw (address recipient, uint256 value);

    /// Constructor.
    function ETHBridge(
        uint256 requiredSignaturesParam,
        address[] authoritiesParam,
        uint256 estimatedGasCostOfWithdrawParam,
        uint256 maxTotalHomeContractBalanceParam,
        uint256 maxSingleDepositValueParam
    ) public
    {
        require(requiredSignaturesParam != 0);
        require(requiredSignaturesParam <= authoritiesParam.length);
        requiredSignatures = requiredSignaturesParam;
        authorities = authoritiesParam;
        estimatedGasCostOfWithdraw = estimatedGasCostOfWithdrawParam;
        maxTotalHomeContractBalance = maxTotalHomeContractBalanceParam;
        maxSingleDepositValue = maxSingleDepositValueParam;
    }

    /// Should be used to deposit money.
    function () public payable {
        //require(maxSingleDepositValue == 0 || msg.value <= maxSingleDepositValue);
        // the value of `this.balance` in payable methods is increased
        // by `msg.value` before the body of the payable method executes
        //require(maxTotalHomeContractBalance == 0 || this.balance <= maxTotalHomeContractBalance);
        Deposit(msg.sender, msg.value);
    }

    /// final step of a withdraw.
    /// checks that `requiredSignatures` `authorities` have signed of on the `message`.
    /// then transfers `value` to `recipient` (both extracted from `message`).
    /// see message library above for a breakdown of the `message` contents.
    /// `vs`, `rs`, `ss` are the components of the signatures.

    /// anyone can call this, provided they have the message and required signatures!
    /// only the `authorities` can create these signatures.
    /// `requiredSignatures` authorities can sign arbitrary `message`s
    /// transfering any ether `value` out of this contract to `recipient`.
    /// bridge users must trust a majority of `requiredSignatures` of the `authorities`.
    function recover(address recipient, uint256 value) public {
        //require(message.length == 116);

        // check that at least `requiredSignatures` `authorities` have signed `message`
        //require(hasEnoughValidSignatures(vs));

        //address recipient = getRecipient(message);
        //uint256 value = getValue(message);
        //bytes32 hash = getTransactionHash(message);
        //uint256 homeGasPrice = getHomeGasPrice(message);

        // if the recipient calls `withdraw` they can choose the gas price freely.
        // if anyone else calls `withdraw` they have to use the gas price
        // `homeGasPrice` specified by the user initiating the withdraw.
        // this is a security mechanism designed to shut down
        // malicious senders setting extremely high gas prices
        // and effectively burning recipients withdrawn value.
        // see https://github.com/paritytech/parity-bridge/issues/112
        // for further explanation.
        //require((recipient == msg.sender) || (tx.gasprice == homeGasPrice));

        // The following two statements guard against reentry into this function.
        // Duplicated withdraw or reentry.
        //require(!withdraws[hash]);
        // Order of operations below is critical to avoid TheDAO-like re-entry bug
        //withdraws[hash] = true;

        //uint256 estimatedWeiCostOfWithdraw = estimatedGasCostOfWithdraw * homeGasPrice;

        // charge recipient for relay cost
        //uint256 valueRemainingAfterSubtractingCost = value - estimatedWeiCostOfWithdraw;

        // pay out recipient
        //recipient.transfer(value);

        // refund relay cost to relaying authority
        //msg.sender.transfer(estimatedWeiCostOfWithdraw);

        Withdraw(recipient, value);
    }

//    function addressArrayContains(address[] array, address value) internal pure returns (bool) {
//        for (uint256 i = 0; i < array.length; i++) {
//            if (array[i] == value) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    // returns the digits of `inputValue` as a string.
//    // example: `uintToString(12345678)` returns `"12345678"`
//    function uintToString(uint256 inputValue) internal pure returns (string) {
//        // figure out the length of the resulting string
//        uint256 length = 0;
//        uint256 currentValue = inputValue;
//        do {
//            length++;
//            currentValue /= 10;
//        } while (currentValue != 0);
//        // allocate enough memory
//        bytes memory result = new bytes(length);
//        // construct the string backwards
//        uint256 i = length - 1;
//        currentValue = inputValue;
//        do {
//            result[i--] = byte(48 + currentValue % 10);
//            currentValue /= 10;
//        } while (currentValue != 0);
//        return string(result);
//    }

    /// returns whether signatures (whose components are in `vs`, `rs`, `ss`)
    /// contain `requiredSignatures` distinct correct signatures
    /// where signer is in `allowed_signers`
    /// that signed `message`
//    function hasEnoughValidSignatures(uint8[] vs) internal view returns (bool) {
//        // not enough signatures
//        if (vs.length < requiredSignatures) {
//            return false;
//        }
//
//        // Withdrawn function is giving infinite gas. So I removing functionality until I found which one is causing problems
////        var hash = hashMessage(message);
////        var encountered_addresses = new address[](allowed_signers.length);
////
////        for (uint256 i = 0; i < requiredSignatures; i++) {
////            var recovered_address = ecrecover(hash, vs[i], rs[i], ss[i]);
////            // only signatures by addresses in `addresses` are allowed
////            if (!addressArrayContains(allowed_signers, recovered_address)) {
////                return false;
////            }
////            // duplicate signatures are not allowed
////            if (addressArrayContains(encountered_addresses, recovered_address)) {
////                return false;
////            }
////            encountered_addresses[i] = recovered_address;
////        }
//        return true;
//    }

//    function recoverAddressFromSignedMessage(bytes signature, bytes message) internal pure returns (address) {
//        require(signature.length == 65);
//        bytes32 r;
//        bytes32 s;
//        bytes1 v;
//        // solium-disable-next-line security/no-inline-assembly
//        assembly {
//            r := mload(add(signature, 0x20))
//            s := mload(add(signature, 0x40))
//            v := mload(add(signature, 0x60))
//        }
//        return ecrecover(hashMessage(message), uint8(v), r, s);
//    }
//
//    function hashMessage(bytes message) internal pure returns (bytes32) {
//        bytes memory prefix = "\x19Ethereum Signed Message:\n";
//        return keccak256(prefix, uintToString(message.length), message);
//    }

    // layout of message :: bytes:
    // offset  0: 32 bytes :: uint256 (big endian) - message length
    // offset 32: 20 bytes :: address - recipient address
    // offset 52: 32 bytes :: uint256 (big endian) - value
    // offset 84: 32 bytes :: bytes32 - transaction hash
    // offset 116: 32 bytes :: uint256 (big endian) - home gas price

    // mload always reads 32 bytes.
    // if mload reads an address it only interprets the last 20 bytes as the address.
    // so we can and have to start reading recipient at offset 20 instead of 32.
    // if we were to read at 32 the address would contain part of value and be corrupted.
    // when reading from offset 20 mload will ignore 12 bytes followed
    // by the 20 recipient address bytes and correctly convert it into an address.
    // this saves some storage/gas over the alternative solution
    // which is padding address to 32 bytes and reading recipient at offset 32.
    // for more details see discussion in:
    // https://github.com/paritytech/parity-bridge/issues/61

//    function getRecipient(bytes message) internal pure returns (address) {
//        address recipient;
//        // solium-disable-next-line security/no-inline-assembly
//        assembly {
//            recipient := mload(add(message, 20))
//        }
//        return recipient;
//    }
//
//    function getValue(bytes message) internal pure returns (uint256) {
//        uint256 value;
//        // solium-disable-next-line security/no-inline-assembly
//        assembly {
//            value := mload(add(message, 52))
//        }
//        return value;
//    }

//    function getTransactionHash(bytes message) internal pure returns (bytes32) {
//        bytes32 hash;
//        // solium-disable-next-line security/no-inline-assembly
//        assembly {
//            hash := mload(add(message, 84))
//        }
//        return hash;
//    }

//    function getHomeGasPrice(bytes message) internal pure returns (uint256) {
//        uint256 gasPrice;
//        // solium-disable-next-line security/no-inline-assembly
//        assembly {
//            gasPrice := mload(add(message, 116))
//        }
//        return gasPrice;
//    }
}