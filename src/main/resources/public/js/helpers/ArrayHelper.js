module.exports.arrayContainsNumber = (array, number) => {
    for(let x = 0; x < array.length; x++){
        if(array[x] === number){
            return true;
        }
    }
    return false;
}

module.exports.removeNumberFromArray = (array, number) => {
    let newArray = [];
    for(let x = 0; x < array.length; x++) {
        if(array[x] === number){
        } else {
            newArray.push(array[x]);
        }
    }
    return newArray;
}

module.exports.addNumberToArray = (array, number) => {
    let newArray = array;

    let foundNumber = false;
    for(let x = 0; x < array.length; x++) {
        if(array[x] === number){
            foundNumber = true;
        }
    }

    if(!foundNumber) {
        newArray.push(number);
    }
    return newArray;
}