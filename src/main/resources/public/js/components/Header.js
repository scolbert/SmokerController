import { Link } from 'react-router-dom';
import React from 'react';

const Header = (props) => {
    return (
        <header>
            <nav>
                <Link to="/">Start Smoking</Link>   -   <Link to="/createSmokingPlan">Create A Smoking Plan</Link>    -   <Link to="/preferences">Preferences</Link>
            </nav>
        </header>
    )
}

export default Header;