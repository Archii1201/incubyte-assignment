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
                name="make"
                value={form.make}
                onChange={handleChange}
                placeholder="Make"
            />

            <br /><br />

            <input
                name="model"
                value={form.model}
                onChange={handleChange}
                placeholder="Model"
            />

            <br /><br />

            <input
                name="category"
                value={form.category}
                onChange={handleChange}
                placeholder="Category"
            />

            <br /><br />

            <input
                type="number"
                name="price"
                value={form.price}
                onChange={handleChange}
                placeholder="Price"
            />

            <br /><br />

            <input
                type="number"
                name="quantity"
                value={form.quantity}
                onChange={handleChange}
                placeholder="Quantity"
            />

            <br /><br />

            <button type="submit">

                Update Vehicle

            </button>

            {error &&

                <p style={{ color: "red" }}>

                    {error}

                </p>
            }

        </form>
    );
}