export function getPlans() {
    return ({
        type: "GET_PLANS"
    });
}

export function addPlan(){
    return ({
        type: "PLAN_ADD",
        payload: {id:3, name:'Added Smoking Plan'}
    })
}