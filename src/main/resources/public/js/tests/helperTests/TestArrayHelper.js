const helper =  require('../../helpers/ArrayHelper.js');
const assert = require('assert');

describe('arrayContainsNumber', function(){
    it('returns true when array contains the number', function(){
        testArray = [2, 3, 4];
        assert(helper.arrayContainsNumber(testArray, 2) === true);
    });
    it('returns false when array does not contain the number', function(){
        testArray = [2, 3, 4];
        assert(helper.arrayContainsNumber(testArray, 1) === false);
    });
});

describe('removeNumberFromArray', function(){
    it('removes number from array when array contains it', function(){
        testArray = [2, 3, 4, 5];
        assert(JSON.stringify(helper.removeNumberFromArray(testArray, 5)) === JSON.stringify([2, 3, 4]));
    })
});

describe('addNumberToArray', function(){
    it('adds number to array when array doesnt contain it', function(){
        testArray = [2, 3, 4];
        assert(JSON.stringify(helper.addNumberToArray(testArray, 5)) === JSON.stringify([2, 3, 4, 5]));
    })
});