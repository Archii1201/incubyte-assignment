import { useEffect, useState } from "react";
import {
    getVehicleById,
    updateVehicle,
} from "../services/vehicleService";
import { useNavigate, useParams } from "react-router-dom";

export default function EditVehicleForm() {

    const { id } = useParams();

    const navigate = useNavigate();

    const [loading, setLoading] = useState(true);

    const [error, setError] = useState("");

    const [form, setForm] = useState({

        make: "",

        model: "",

        category: "",

        price: "",

        quantity: "",
    });

    useEffect(() => {

        loadVehicle();

    }, []);

    const loadVehicle = async () => {

        try {

            const vehicle =
                await getVehicleById(id);

            setForm({

                make: vehicle.make,

                model: vehicle.model,

                category: vehicle.category,

                price: vehicle.price,

                quantity: vehicle.quantity,
            });

        } catch (err) {

            setError("Failed to load vehicle.");

        } finally {

            setLoading(false);
        }
    };

    const handleChange = (e) => {

        setForm({

            ...form,

            [e.target.name]: e.target.value,
        });
    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            await updateVehicle(id, {

                ...form,

                price: Number(form.price),

                quantity: Number(form.quantity),
            });

            alert("Vehicle updated successfully.");

            navigate("/vehicles");

        } catch (err) {

            setError(

                err.response?.data?.message ||

                "Update failed."
            );
        }
    };

    if (loading) {

        return <h2>Loading...</h2>;
    }

    return (

    <form onSubmit={handleSubmit}>

        <input
            className="input"
            name="make"
            value={form.make}
            onChange={handleChange}
            placeholder="Vehicle Make"
        />

        <input
            className="input"
            name="model"
            value={form.model}
            onChange={handleChange}
            placeholder="Vehicle Model"
        />

        <input
            className="input"
            name="category"
            value={form.category}
            onChange={handleChange}
            placeholder="Category"
        />

        <input
            className="input"
            type="number"
            name="price"
            value={form.price}
            onChange={handleChange}
            placeholder="Price"
        />

        <input
            className="input"
            type="number"
            name="quantity"
            value={form.quantity}
            onChange={handleChange}
            placeholder="Quantity"
        />

        <div
            style={{
                display: "flex",
                justifyContent: "space-between",
                marginTop: "20px",
            }}
        >

            <button
                type="button"
                className="btn btn-outline"
                onClick={() => navigate("/vehicles")}
            >
                Cancel
            </button>

            <button
                className="btn btn-primary"
                type="submit"
            >
                Update Vehicle
            </button>

        </div>

        {error && (

            <p
                style={{
                    color: "#ff8a8a",
                    marginTop: "20px",
                    textAlign: "center",
                }}
            >
                {error}
            </p>

        )}

    </form>

);
}