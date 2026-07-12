import { NavLink, useNavigate } from "react-router-dom";

export default function Navbar() {

    const navigate = useNavigate();

    const role = localStorage.getItem("role");

    const logout = () => {

        localStorage.clear();

        navigate("/login");

    };

    return (

        <nav className="navbar">

            <div className="logo">

                🚗 AutoHub

            </div>

            <div className="nav-links">

                <NavLink to="/dashboard">

                    Dashboard

                </NavLink>

                <NavLink to="/vehicles">

                    Inventory

                </NavLink>

                

                    <NavLink to="/vehicles/add">

                        Add Vehicle

                    </NavLink>

                

                {/* <NavLink to="/transactions">

                    Transactions

                </NavLink> */}

            </div>

            <button
                className="btn btn-danger"
                onClick={logout}
            >

                Logout

            </button>

        </nav>

    );

}