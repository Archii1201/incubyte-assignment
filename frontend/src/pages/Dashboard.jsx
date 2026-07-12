import { Link } from "react-router-dom";
import { useAuth } from "../hooks/useAuth";
import Layout from "../components/Layout";

export default function Dashboard() {

    const { user } = useAuth();

    return (

        <Layout>

            <div className="container">

                <h1 className="page-title">
                    Dashboard
                </h1>

                <div className="card section">

                    <h2>
                        Welcome, {user?.email}
                    </h2>

                    <p
                        style={{
                            marginTop: "10px",
                            color: "#9C7B7B",
                        }}
                    >
                        Logged in as <strong>{user?.role}</strong>
                    </p>

                </div>

                <div
                    className="dashboard-grid"
                >

                    <div className="dashboard-card">

                        <h3>Total Vehicles</h3>

                        <h1>25</h1>

                    </div>

                    <div className="dashboard-card">

                        <h3>Low Stock</h3>

                        <h1>3</h1>

                    </div>

                    <div className="dashboard-card">

                        <h3>Purchases</h3>

                        <h1>18</h1>

                    </div>

                    <div className="dashboard-card">

                        <h3>Inventory Value</h3>

                        <h1>₹4.2L</h1>

                    </div>

                </div>

                <div
                    className="card section"
                    style={{
                        marginTop: "35px",
                    }}
                >

                    <h2>
                        Quick Actions
                    </h2>

                    <div
                        style={{
                            display: "flex",
                            gap: "15px",
                            flexWrap: "wrap",
                            marginTop: "20px",
                        }}
                    >

                        <Link to="/vehicles">

                            <button className="btn btn-primary">

                                Vehicle Inventory

                            </button>

                        </Link>

                        { (

                            <Link to="/vehicles/add">

                                <button className="btn btn-success">

                                    Add Vehicle

                                </button>

                            </Link>

                        )}

                        {/* <Link to="/transactions">

                            <button className="btn btn-outline">

                                Transactions

                            </button>

                        </Link> */}

                    </div>

                </div>

            </div>

        </Layout>

    );

}