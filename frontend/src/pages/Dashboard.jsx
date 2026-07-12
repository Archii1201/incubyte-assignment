import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import Layout from "../components/Layout";

export default function Dashboard() {

    const { user } = useAuth();

    const [now, setNow] = useState(new Date());

    useEffect(() => {

        const timer = setInterval(() => {

            setNow(new Date());

        }, 1000);

        return () => clearInterval(timer);

    }, []);

    const hour = now.getHours();

    const greeting =
        hour < 12
            ? "Good Morning"
            : hour < 17
            ? "Good Afternoon"
            : "Good Evening";

    const currentDate = now.toLocaleDateString(
        "en-IN",
        {
            weekday: "long",
            year: "numeric",
            month: "long",
            day: "numeric",
        }
    );

    const currentTime = now.toLocaleTimeString(
        "en-IN",
        {
            hour: "2-digit",
            minute: "2-digit",
            second: "2-digit",
        }
    );

    return (

        <Layout>

            <div className="container">

                <div className="card section">

                    <h1 className="page-title">
                        {greeting}, {user?.email}
                    </h1>

                    <p
                        style={{
                            color: "#9C7B7B",
                            marginTop: "10px",
                            fontSize: "15px",
                        }}
                    >
                        Logged in as{" "}
                        <strong
                            style={{
                                color: "#E64435",
                            }}
                        >
                            {user?.role}
                        </strong>
                    </p>

                    <div
                        style={{
                            marginTop: "20px",
                            display: "flex",
                            justifyContent: "space-between",
                            flexWrap: "wrap",
                            gap: "10px",
                        }}
                    >

                        <div>

                            <p
                                style={{
                                    color: "#9C7B7B",
                                    marginBottom: "5px",
                                }}
                            >
                                Today
                            </p>

                            <h3>{currentDate}</h3>

                        </div>

                        <div
                            style={{
                                textAlign: "right",
                            }}
                        >

                            <p
                                style={{
                                    color: "#9C7B7B",
                                    marginBottom: "5px",
                                }}
                            >
                                Current Time
                            </p>

                            <h2
                                style={{
                                    color: "#E64435",
                                }}
                            >
                                {currentTime}
                            </h2>

                        </div>

                    </div>

                </div>

                <h2
                    style={{
                        marginTop: "35px",
                        marginBottom: "20px",
                    }}
                >
                    System Overview
                </h2>

                <div className="dashboard-grid">

                    <div className="dashboard-card">

                        <h3>Total Vehicles</h3>

                        <h1>25</h1>

                        <p>Registered Vehicles</p>

                    </div>

                    <div className="dashboard-card">

                        <h3>Low Stock</h3>

                        <h1>3</h1>

                        <p>Need Restocking</p>

                    </div>

                    <div className="dashboard-card">

                        <h3>Out of Stock</h3>

                        <h1>1</h1>

                        <p>Unavailable Vehicles</p>

                    </div>

                    <div className="dashboard-card">

                        <h3>Inventory Value</h3>

                        <h1>₹4.2L</h1>

                        <p>Total Inventory Worth</p>

                    </div>

                </div>

                <div
                    className="card section"
                    style={{
                        marginTop: "40px",
                    }}
                >

                    <h2>Quick Actions</h2>

                    <div
                        style={{
                            display: "flex",
                            gap: "15px",
                            flexWrap: "wrap",
                            marginTop: "20px",
                        }}
                    >

                        <Link to="/vehicles">

                            <button
                                className="btn btn-primary"
                            >
                                🚗 Vehicle Inventory
                            </button>

                        </Link>

                        

                            <Link to="/vehicles/add">

                                <button
                                    className="btn btn-success"
                                >
                                    ➕ Add Vehicle
                                </button>

                            </Link>

                       

                    </div>

                </div>

            </div>

        </Layout>

    );

}