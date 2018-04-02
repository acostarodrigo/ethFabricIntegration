pragma solidity ^0.4.0;

/**
* Used to limit methods calls to the owner of the contract.
* Certain functions can only be called by the creator. Ownership can be transfered too.
*/
contract owned {
    address public owner;

    function owned() public {
        owner = msg.sender;
    }

    modifier onlyOwner {
        require(msg.sender == owner);
        _;
    }

    function transferOwnership(address newOwner) onlyOwner public {
        owner = newOwner;
    }
}