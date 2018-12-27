pragma solidity ^0.4.24;

contract HelloWorld {
    address public owner;

    constructor() public {
        owner = msg.sender;
    }

    function hello() public pure returns (string) {
        return "Hello world";
    }
}
