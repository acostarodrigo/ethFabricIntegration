package BridgeIntegration.contracts;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
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
    private static final String BINARY = "6060604052341561000f57600080fd5b610a928061001e6000396000f3006060604052600436106100825763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416632a5c0e7c811461010a5780633c3aa8391461016d578063494503d41461019257806367faa519146101c457806382bbe365146101d75780638d068043146101ea5780639ce318f6146101fd575b600354158061009357506003543411155b151561009e57600080fd5b60025415806100b9575060025430600160a060020a03163111155b15156100c457600080fd5b7fe1fffcc4923d04b559f4d29a8bfc6cda04eb5b0d3c460751c2402c5c5cc9109c3334604051600160a060020a03909216825260208201526040908101905180910390a1005b341561011557600080fd5b61016b60048035906044602480359081019083013580602081810201604051908101604052809392919081815260200183836020028082843750949650508435946020810135945060400135925061030e915050565b005b341561017857600080fd5b610180610351565b60405190815260200160405180910390f35b341561019d57600080fd5b6101a8600435610357565b604051600160a060020a03909116815260200160405180910390f35b34156101cf57600080fd5b61018061037f565b34156101e257600080fd5b610180610385565b34156101f557600080fd5b61018061038b565b341561020857600080fd5b61016b6004602481358181019083013580602081810201604051908101604052809392919081815260200183836020028082843782019150505050505091908035906020019082018035906020019080806020026020016040519081016040528093929190818152602001838360200280828437820191505050505050919080359060200190820180359060200190808060200260200160405190810160405280939291908181526020018383602002808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284375094965061039195505050505050565b84151561031a57600080fd5b835185111561032857600080fd5b600085905560048480516103409291602001906109ac565b506001929092556002556003555050565b60025481565b600480548290811061036557fe5b600091825260209091200154600160a060020a0316905081565b60035481565b60015481565b60005481565b60008060008060008086516074146103a857600080fd5b610412878b8b8b600480548060200260200160405190810160405280929190818152602001828054801561040557602002820191906000526020600020905b8154600160a060020a031681526001909101906020018083116103e7575b505050505060005461056a565b151561041d57600080fd5b610426876106e6565b9550610431876106f5565b945061043c87610704565b935061044787610713565b925033600160a060020a031686600160a060020a031614806104685750823a145b151561047357600080fd5b60008481526005602052604090205460ff161561048f57600080fd5b505060008281526005602052604090819020805460ff191660019081179091555482029081850390600160a060020a0387169082156108fc0290839051600060405180830381858888f1935050505015156104e957600080fd5b600160a060020a03331682156108fc0283604051600060405180830381858888f19350505050151561051a57600080fd5b7f884edad9ce6fa2440d8a54cc123490eb96d2768479d49ff9c7366125a94243648682604051600160a060020a03909216825260208201526040908101905180910390a150505050505050505050565b600080610575610a20565b600080858a51101561058a57600094506106d8565b6105938b610722565b935086516040518059106105a45750595b90808252806020026020018201604052509250600091505b858210156106d3576001848b84815181106105d357fe5b906020019060200201518b85815181106105e957fe5b906020019060200201518b86815181106105ff57fe5b906020019060200201516040516000815260200160405260006040516020015260405193845260ff90921660208085019190915260408085019290925260608401929092526080909201915160208103908084039060008661646e5a03f1151561066857600080fd5b505060206040510351905061067d878261086b565b151561068c57600094506106d8565b610696838261086b565b156106a457600094506106d8565b808383815181106106b157fe5b600160a060020a039092166020928302909101909101526001909101906105bc565b600194505b505050509695505050505050565b60008060148301519392505050565b60008060348301519392505050565b60008060548301519392505050565b60008060748301519392505050565b600061072c610a20565b60408051908101604052601a81527f19457468657265756d205369676e6564204d6573736167653a0a000000000000602082015290508061076d84516108c4565b846040518084805190602001908083835b6020831061079d5780518252601f19909201916020918201910161077e565b6001836020036101000a038019825116818451161790925250505091909101905083805190602001908083835b602083106107e95780518252601f1990920191602091820191016107ca565b6001836020036101000a038019825116818451161790925250505091909101905082805190602001908083835b602083106108355780518252601f199092019160209182019101610816565b6001836020036101000a038019825116818451161790925250505091909101945060409350505050518091039020915050919050565b6000805b83518110156108b85782600160a060020a031684828151811061088e57fe5b90602001906020020151600160a060020a031614156108b057600191506108bd565b60010161086f565b600091505b5092915050565b6108cc610a20565b6000806108d7610a20565b60008093508592505b600190930192600a8304925082156108f7576108e0565b836040518059106109055750595b818152601f19601f8301168101602001604052905086935091505060001983015b6000198101907f01000000000000000000000000000000000000000000000000000000000000006030600a860601029083908151811061096257fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350600a8304925082156109a357610926565b50949350505050565b828054828255906000526020600020908101928215610a10579160200282015b82811115610a10578251825473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0391909116178255602092909201916001909101906109cc565b50610a1c929150610a32565b5090565b60206040519081016040526000815290565b610a6391905b80821115610a1c57805473ffffffffffffffffffffffffffffffffffffffff19168155600101610a38565b905600a165627a7a72305820f092c9ba45f4fb36515338daf9a458458d8a5c5e8bcdbdc49da7fa84d9c9d0e90029";

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

    public RemoteCall<TransactionReceipt> HomeBridge(BigInteger requiredSignaturesParam, List<String> authoritiesParam, BigInteger estimatedGasCostOfWithdrawParam, BigInteger maxTotalHomeContractBalanceParam, BigInteger maxSingleDepositValueParam) {
        Function function = new Function(
                "HomeBridge", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(requiredSignaturesParam), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.Utils.typeMap(authoritiesParam, org.web3j.abi.datatypes.Address.class)), 
                new org.web3j.abi.datatypes.generated.Uint256(estimatedGasCostOfWithdrawParam), 
                new org.web3j.abi.datatypes.generated.Uint256(maxTotalHomeContractBalanceParam), 
                new org.web3j.abi.datatypes.generated.Uint256(maxSingleDepositValueParam)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
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

    public static RemoteCall<ETHBridge> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ETHBridge.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<ETHBridge> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(ETHBridge.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
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
