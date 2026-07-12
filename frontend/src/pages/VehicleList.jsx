import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import Layout from "../components/Layout";
import {
    searchVehicles,
    deleteVehicle,
    purchaseVehicle,
    restockVehicle,
} from "../services/vehicleService";

export default function VehicleList() {

    const [vehicles, setVehicles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [search, setSearch] = useState({
        make: "",
        model: "",
        category: "",
    });

    const [page, setPage] = useState(0);
    const [size] = useState(5);
    const [totalPages, setTotalPages] = useState(0);

    const role = localStorage.getItem("role");

    useEffect(() => {
        loadVehicles();
    }, [page]);

    const loadVehicles = async () => {

        setLoading(true);
        setError("");

        try {

            const params = {
                page,
                size,
            };

            if (search.make.trim() !== "")
                params.make = search.make;

            if (search.model.trim() !== "")
                params.model = search.model;

            if (search.category.trim() !== "")
                params.category = search.category;

            const data = await searchVehicles(params);

            setVehicles(data.content);
            setTotalPages(data.totalPages);

        } catch (error) {

            setError("Failed to load vehicles.");
            console.error(error);

        } finally {

            setLoading(false);
        }
    };

    const handleSearch = () => {
        setPage(0);
        loadVehicles();
    };

    const handlePurchase = async (id) => {

        if (!window.confirm("Purchase this vehicle?"))
            return;

        try {

            await purchaseVehicle(id, 1);

            await loadVehicles();

            alert("Purchase successful.");

        } catch (error) {

            alert(
                error.response?.data?.message ||
                "Purchase failed."
            );
        }
    };

    const handleRestock = async (id) => {

        const quantity = window.prompt(
            "Enter quantity to restock:"
        );

        if (quantity === null)
            return;

        try {

            await restockVehicle(
                id,
                Number(quantity)
            );

            await loadVehicles();

            alert("Vehicle restocked successfully.");

        } catch (error) {

            alert(
                error.response?.data?.message ||
                "Restock failed."
            );
        }
    };

    const handleDelete = async (id) => {

        if (!window.confirm(
            "Are you sure you want to delete this vehicle?"
        ))
            return;

        try {

            await deleteVehicle(id);

            await loadVehicles();

            alert("Vehicle deleted successfully.");

        } catch (error) {

            alert(
                error.response?.data?.message ||
                "Delete failed."
            );
        }
    };

    if (loading)
        return <h2 className="container">Loading vehicles...</h2>;

    if (error)
        return <h2 className="container">{error}</h2>;

    return (
        <Layout>
        <div className="container">

            <h1 className="page-title">
                🚗 Vehicle Inventory
            </h1>

            <div className="card section">

                <input
                    className="input"
                    placeholder="Make"
                    value={search.make}
                    onChange={(e) =>
                        setSearch({
                            ...search,
                            make: e.target.value,
                        })
                    }
                />

                <input
                    className="input"
                    placeholder="Model"
                    value={search.model}
                    onChange={(e) =>
                        setSearch({
                            ...search,
                            model: e.target.value,
                        })
                    }
                />

                <input
                    className="input"
                    placeholder="Category"
                    value={search.category}
                    onChange={(e) =>
                        setSearch({
                            ...search,
                            category: e.target.value,
                        })
                    }
                />

                <button
                    className="btn btn-primary"
                    onClick={handleSearch}
                >
                    Search
                </button>

            </div>

            {vehicles.length === 0 ? (

                <div className="card">

                    <h3>No vehicles found.</h3>

                </div>

            ) : (

               vehicles.map((vehicle) => (

    <div
        key={vehicle.id}
        className="vehicle-card"
    >

        <div className="vehicle-header">

            <div>

                <h2 className="vehicle-name">
                    {vehicle.make} {vehicle.model}
                </h2>

                <div
                    style={{
                        marginTop: "10px",
                    }}
                >

                    <span
                        className={
                            vehicle.quantity === 0
                                ? "badge badge-out"
                                : "badge badge-active"
                        }
                    >
                        {vehicle.quantity === 0
                            ? "Out of Stock"
                            : "Active"}
                    </span>

                </div>

            </div>

            <div className="vehicle-price">

                ₹{Number(vehicle.price).toLocaleString("en-IN")}

            </div>

        </div>

        <div className="vehicle-details">

            <div>

                <div className="detail-label">
                    Category
                </div>

                <div className="detail-value">
                    {vehicle.category}
                </div>

            </div>

            <div>

                <div className="detail-label">
                    Available Stock
                </div>

                <span
                    className={
                        vehicle.quantity === 0
                            ? "badge badge-empty"
                            : vehicle.quantity <= 5
                            ? "badge badge-low"
                            : "badge badge-stock"
                    }
                >
                    {vehicle.quantity} Vehicles
                </span>

            </div>

        </div>

        <div className="vehicle-actions">

            <button
                className={
                    vehicle.quantity === 0
                        ? "btn btn-dark"
                        : "btn btn-success"
                }
                disabled={vehicle.quantity === 0}
                onClick={() =>
                    handlePurchase(vehicle.id)
                }
            >
                {vehicle.quantity === 0
                    ? "Out of Stock"
                    : "Purchase"}
            </button>

            <Link
                to={`/vehicles/edit/${vehicle.id}`}
            >
                <button className="btn btn-outline">
                    Edit
                </button>
            </Link>

            {role === "ADMIN" && (

                <>

                    <button
                        className="btn btn-primary"
                        onClick={() =>
                            handleRestock(vehicle.id)
                        }
                    >
                        Restock
                    </button>

                    <button
                        className="btn btn-danger"
                        onClick={() =>
                            handleDelete(vehicle.id)
                        }
                    >
                        Delete
                    </button>

                </>

            )}

        </div>

    </div>

))

            )}

            <div
                style={{
                    display: "flex",
                    justifyContent: "space-between",
                    marginTop: "30px",
                }}
            >

                <button
                    className="btn btn-dark"
                    disabled={page === 0}
                    onClick={() =>
                        setPage(page - 1)
                    }
                >
                    Previous
                </button>

                <button
                    className="btn btn-dark"
                    disabled={page >= totalPages - 1}
                    onClick={() =>
                        setPage(page + 1)
                    }
                >
                    Next
                </button>

            </div>

        </div>
        </Layout>
    );
}