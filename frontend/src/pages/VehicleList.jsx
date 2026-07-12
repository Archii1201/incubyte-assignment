import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

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

            if (search.make.trim() !== "") {
                params.make = search.make;
            }

            if (search.model.trim() !== "") {
                params.model = search.model;
            }

            if (search.category.trim() !== "") {
                params.category = search.category;
            }

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

        const confirmed = window.confirm(
            "Purchase this vehicle?"
        );

        if (!confirmed) {
            return;
        }

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

    if (quantity === null) {
        return;
    }

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

        const confirmed = window.confirm(
            "Are you sure you want to delete this vehicle?"
        );

        if (!confirmed) {
            return;
        }

        try {

            await deleteVehicle(id);

            await loadVehicles();

            alert("Vehicle deleted successfully.");

        } catch (error) {

            alert(
                error.response?.data?.message ||
                "Failed to delete vehicle."
            );
        }
    };

    if (loading) {
        return <h2>Loading vehicles...</h2>;
    }

    if (error) {
        return <h2>{error}</h2>;
    }

    return (

        <div>

            <h1>Vehicle Inventory</h1>

            <div>

                <input
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
                    placeholder="Category"
                    value={search.category}
                    onChange={(e) =>
                        setSearch({
                            ...search,
                            category: e.target.value,
                        })
                    }
                />

                <button onClick={handleSearch}>
                    Search
                </button>

            </div>

            {vehicles.length === 0 ? (

                <p>No vehicles found.</p>

            ) : (

                vehicles.map(vehicle => (

                    <div key={vehicle.id}>

                        <p>{vehicle.make}</p>

                        <p>{vehicle.model}</p>

                        <p>{vehicle.price}</p>

                        <p>Available: {vehicle.quantity}</p>

                        <button
                            disabled={vehicle.quantity === 0}
                            onClick={() => handlePurchase(vehicle.id)}
                        >
                            {vehicle.quantity === 0
                                ? "Out of Stock"
                                : "Purchase"}
                        </button>

                        <Link to={`/vehicles/edit/${vehicle.id}`}>
                            <button>Edit</button>
                        </Link>

                       {role === "ADMIN" && (

                        <>

                            <button
                                onClick={() => handleRestock(vehicle.id)}
                            >
                                Restock
                            </button>

                            <button
                                onClick={() => handleDelete(vehicle.id)}
                            >
                                Delete
                            </button>

                        </>

                    )}

                    </div>

                ))

            )}

            <br />

            <button
                disabled={page === 0}
                onClick={() => setPage(page - 1)}
            >
                Previous
            </button>

            <button
                disabled={page >= totalPages - 1}
                onClick={() => setPage(page + 1)}
            >
                Next
            </button>

        </div>
    );
}