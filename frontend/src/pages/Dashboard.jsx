import { useNavigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";

export default function Dashboard() {

    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () => {

        logout();

        navigate("/");
    };

    return (

        <div>

            <h1>Dashboard</h1>

            <p>
                Welcome {user?.email}
            </p>

            <p>
                Role: {user?.role}
            </p>

            <button onClick={handleLogout}>
                Logout
            </button>

        </div>
    );
}