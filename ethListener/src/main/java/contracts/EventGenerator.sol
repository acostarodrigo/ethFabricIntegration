pragma solidity ^0.4.0;

contract EventGenerator {
    /// Event created on money deposit.
    event Deposit (address recipient, uint256 value);

    /// Event created on money withdrawn
    event Withdrawn (address sender, uint256 value);

    /// the addresses of authorities that can allow the withdrawn
    address[] public authorities;
    /// the amount of signatures we will be requiring
    uint256 public requiredSignatures;

    /// constructor
    function EventGenerator(address[] authorities_param, uint256 requiredSignatures_param) public{
        for (uint i = 0; i < authorities_param.length;i++){
            authorities.push(authorities_param[i]);
        }

        requiredSignatures = requiredSignatures_param;

    }

    /// Should be used to deposit money.
    function () public payable {
        Deposit(msg.sender, msg.value);
    }

    /// will be used to withdrawn the money
    function withdrawn(uint256 value) public{
        Withdrawn(msg.sender, value);
    }

    function getAuthority(uint index) public constant returns (address) {
        return authorities[index];
    }
}
