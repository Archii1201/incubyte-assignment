import { useEffect, useState } from "react";

import {
    getVehicles,
    deleteVehicle
} from "../services/vehicleService";
import { Link } from "react-router-dom";
export default function VehicleList() {

    const [vehicles, setVehicles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const role = localStorage.getItem("role");
    useEffect(() => {
        loadVehicles();
    }, []);

    const loadVehicles = async () => {

        try {

            const data = await getVehicles();

            setVehicles(data);

        } catch (error) {

            setError("Failed to load vehicles.");

            console.error(error);

        } finally {

            setLoading(false);
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

    if (vehicles.length === 0) {
        return (
            <div>
                <h2>Vehicle Inventory</h2>
                <p>No vehicles found.</p>
            </div>
        );
    }

    return (
        <div>

            <h1>Vehicle Inventory</h1>

            {vehicles.map(vehicle => (

                <div key={vehicle.id}>

                    <p>{vehicle.make}</p>
                    <p>{vehicle.model}</p>
                    <p>{vehicle.price}</p>
                    <Link to={`/vehicles/edit/${vehicle.id}`}>
                        <button>Edit</button>
                    </Link>
                    {role === "ADMIN" && (
                            <button
                                onClick={() => handleDelete(vehicle.id)}
                            >
                                Delete
                            </button>

                    )}
                </div>

            ))}

        </div>
    );
}