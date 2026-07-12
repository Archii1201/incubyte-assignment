import { useState } from "react";
import { createVehicle } from "../services/vehicleService";

export default function AddVehicleForm() {

    const [form, setForm] = useState({
        make: "",
        model: "",
        category: "",
        price: "",
        quantity: "",
    });

    const handleChange = (e) => {

        setForm({

            ...form,

            [e.target.name]: e.target.value,
        });
    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            await createVehicle({

                ...form,

                price: Number(form.price),

                quantity: Number(form.quantity),
            });

            alert("Vehicle added successfully.");

            setForm({

                make: "",

                model: "",

                category: "",

                price: "",

                quantity: "",
            });

        } catch (error) {

            alert(
                error.response?.data?.message ||
                "Failed to add vehicle."
            );
        }
    };

    return (

    <form onSubmit={handleSubmit}>

        <input
            className="input"
            name="make"
            placeholder="Vehicle Make"
            value={form.make}
            onChange={handleChange}
        />

        <input
            className="input"
            name="model"
            placeholder="Vehicle Model"
            value={form.model}
            onChange={handleChange}
        />

        <input
            className="input"
            name="category"
            placeholder="Category"
            value={form.category}
            onChange={handleChange}
        />

        <input
            className="input"
            type="number"
            name="price"
            placeholder="Price"
            value={form.price}
            onChange={handleChange}
        />

        <input
            className="input"
            type="number"
            name="quantity"
            placeholder="Quantity"
            value={form.quantity}
            onChange={handleChange}
        />

        <div
            style={{
                display: "flex",
                justifyContent: "flex-end",
                marginTop: "20px",
            }}
        >

            <button
                className="btn btn-primary"
                type="submit"
            >
                Add Vehicle
            </button>

        </div>

    </form>

);
}