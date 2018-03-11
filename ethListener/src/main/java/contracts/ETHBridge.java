package contracts;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.0.1.
 */
public final class ETHBridge extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b604051610a65380380610a658339810160405280805191906020018051820191906020018051919060200180519190602001805191505084151561005257600080fd5b835185111561006057600080fd5b6000859055600484805161007892916020019061008d565b506001929092556002556003555061011b9050565b8280548282559060005260206000209081019282156100e4579160200282015b828111156100e45782518254600160a060020a031916600160a060020a0391909116178255602092909201916001909101906100ad565b506100f09291506100f4565b5090565b61011891905b808211156100f0578054600160a060020a03191681556001016100fa565b90565b61093b8061012a6000396000f3006060604052600436106100775763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416633c3aa83981146100ff578063494503d41461012457806367faa5191461015657806382bbe365146101695780638d0680431461017c5780639ce318f61461018f575b600354158061008857506003543411155b151561009357600080fd5b60025415806100ae575060025430600160a060020a03163111155b15156100b957600080fd5b7fe1fffcc4923d04b559f4d29a8bfc6cda04eb5b0d3c460751c2402c5c5cc9109c3334604051600160a060020a03909216825260208201526040908101905180910390a1005b341561010a57600080fd5b6101126102a2565b60405190815260200160405180910390f35b341561012f57600080fd5b61013a6004356102a8565b604051600160a060020a03909116815260200160405180910390f35b341561016157600080fd5b6101126102d0565b341561017457600080fd5b6101126102d6565b341561018757600080fd5b6101126102dc565b341561019a57600080fd5b6102a06004602481358181019083013580602081810201604051908101604052809392919081815260200183836020028082843782019150505050505091908035906020019082018035906020019080806020026020016040519081016040528093929190818152602001838360200280828437820191505050505050919080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509190803590602001908201803590602001908080601f0160208091040260200160405190810160405281815292919060208401838380828437509496506102e295505050505050565b005b60025481565b60048054829081106102b657fe5b600091825260209091200154600160a060020a0316905081565b60035481565b60015481565b60005481565b60008060008060008086516074146102f957600080fd5b610363878b8b8b600480548060200260200160405190810160405280929190818152602001828054801561035657602002820191906000526020600020905b8154600160a060020a03168152600190910190602001808311610338575b50505050506000546104bb565b151561036e57600080fd5b61037787610637565b955061038287610646565b945061038d87610655565b935061039887610664565b925033600160a060020a031686600160a060020a031614806103b95750823a145b15156103c457600080fd5b60008481526005602052604090205460ff16156103e057600080fd5b505060008281526005602052604090819020805460ff191660019081179091555482029081850390600160a060020a0387169082156108fc0290839051600060405180830381858888f19350505050151561043a57600080fd5b600160a060020a03331682156108fc0283604051600060405180830381858888f19350505050151561046b57600080fd5b7f884edad9ce6fa2440d8a54cc123490eb96d2768479d49ff9c7366125a94243648682604051600160a060020a03909216825260208201526040908101905180910390a150505050505050505050565b6000806104c66108fd565b600080858a5110156104db5760009450610629565b6104e48b610673565b935086516040518059106104f55750595b90808252806020026020018201604052509250600091505b85821015610624576001848b848151811061052457fe5b906020019060200201518b858151811061053a57fe5b906020019060200201518b868151811061055057fe5b906020019060200201516040516000815260200160405260006040516020015260405193845260ff90921660208085019190915260408085019290925260608401929092526080909201915160208103908084039060008661646e5a03f115156105b957600080fd5b50506020604051035190506105ce87826107bc565b15156105dd5760009450610629565b6105e783826107bc565b156105f55760009450610629565b8083838151811061060257fe5b600160a060020a0390921660209283029091019091015260019091019061050d565b600194505b505050509695505050505050565b60008060148301519392505050565b60008060348301519392505050565b60008060548301519392505050565b60008060748301519392505050565b600061067d6108fd565b60408051908101604052601a81527f19457468657265756d205369676e6564204d6573736167653a0a00000000000060208201529050806106be8451610815565b846040518084805190602001908083835b602083106106ee5780518252601f1990920191602091820191016106cf565b6001836020036101000a038019825116818451161790925250505091909101905083805190602001908083835b6020831061073a5780518252601f19909201916020918201910161071b565b6001836020036101000a038019825116818451161790925250505091909101905082805190602001908083835b602083106107865780518252601f199092019160209182019101610767565b6001836020036101000a038019825116818451161790925250505091909101945060409350505050518091039020915050919050565b6000805b83518110156108095782600160a060020a03168482815181106107df57fe5b90602001906020020151600160a060020a03161415610801576001915061080e565b6001016107c0565b600091505b5092915050565b61081d6108fd565b6000806108286108fd565b60008093508592505b600190930192600a83049250821561084857610831565b836040518059106108565750595b818152601f19601f8301168101602001604052905086935091505060001983015b6000198101907f01000000000000000000000000000000000000000000000000000000000000006030600a86060102908390815181106108b357fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350600a8304925082156108f457610877565b50949350505050565b602060405190810160405260008152905600a165627a7a72305820335cddbc85bb8583b9d6f0029e85cbe291a443be3fb8fd062267d454835d97990029";

    private ETHBridge(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    private ETHBridge(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<DepositEventResponse> getDepositEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Deposit", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<DepositEventResponse> responses = new ArrayList<DepositEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            DepositEventResponse typedResponse = new DepositEventResponse();
            typedResponse.recipient = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<DepositEventResponse> depositEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Deposit", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, DepositEventResponse>() {
            @Override
            public DepositEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                DepositEventResponse typedResponse = new DepositEventResponse();
                typedResponse.recipient = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<WithdrawEventResponse> getWithdrawEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Withdraw", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<WithdrawEventResponse> responses = new ArrayList<WithdrawEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            WithdrawEventResponse typedResponse = new WithdrawEventResponse();
            typedResponse.recipient = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<WithdrawEventResponse> withdrawEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Withdraw", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, WithdrawEventResponse>() {
            @Override
            public WithdrawEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                WithdrawEventResponse typedResponse = new WithdrawEventResponse();
                typedResponse.recipient = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<BigInteger> maxTotalHomeContractBalance() {
        Function function = new Function("maxTotalHomeContractBalance", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> authorities(BigInteger param0) {
        Function function = new Function("authorities", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> maxSingleDepositValue() {
        Function function = new Function("maxSingleDepositValue", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> estimatedGasCostOfWithdraw() {
        Function function = new Function("estimatedGasCostOfWithdraw", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> requiredSignatures() {
        Function function = new Function("requiredSignatures", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> withdraw(List<BigInteger> vs, List<byte[]> rs, List<byte[]> ss, byte[] message) {
        Function function = new Function(
                "withdraw", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint8>(
                        org.web3j.abi.Utils.typeMap(vs, org.web3j.abi.datatypes.generated.Uint8.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                        org.web3j.abi.Utils.typeMap(rs, org.web3j.abi.datatypes.generated.Bytes32.class)), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Bytes32>(
                        org.web3j.abi.Utils.typeMap(ss, org.web3j.abi.datatypes.generated.Bytes32.class)), 
                new org.web3j.abi.datatypes.DynamicBytes(message)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<ETHBridge> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger requiredSignaturesParam, List<String> authoritiesParam, BigInteger estimatedGasCostOfWithdrawParam, BigInteger maxTotalHomeContractBalanceParam, BigInteger maxSingleDepositValueParam) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(requiredSignaturesParam), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(authoritiesParam, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.generated.Uint256(estimatedGasCostOfWithdrawParam), 
                new org.web3j.abi.datatypes.generated.Uint256(maxTotalHomeContractBalanceParam), 
                new org.web3j.abi.datatypes.generated.Uint256(maxSingleDepositValueParam)));
        return deployRemoteCall(ETHBridge.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<ETHBridge> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger requiredSignaturesParam, List<String> authoritiesParam, BigInteger estimatedGasCostOfWithdrawParam, BigInteger maxTotalHomeContractBalanceParam, BigInteger maxSingleDepositValueParam) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(requiredSignaturesParam), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(authoritiesParam, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.generated.Uint256(estimatedGasCostOfWithdrawParam), 
                new org.web3j.abi.datatypes.generated.Uint256(maxTotalHomeContractBalanceParam), 
                new org.web3j.abi.datatypes.generated.Uint256(maxSingleDepositValueParam)));
        return deployRemoteCall(ETHBridge.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static ETHBridge load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ETHBridge(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static ETHBridge load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ETHBridge(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class DepositEventResponse {
        public String recipient;

        public BigInteger value;
    }

    public static class WithdrawEventResponse {
        public String recipient;

        public BigInteger value;
    }
}
