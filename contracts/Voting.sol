pragma solidity ^0.4.0;

contract Voting {

    uint[100] public candidates;

    function vote(uint candidateId) public returns (uint) {
        require(candidateId >= 0 && candidateId <= 99);

        candidates[candidateId] += 1;

        return candidateId;
    }
}
