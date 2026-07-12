import AddVehicleForm from "../components/AddVehicleForm";
import Layout from "../components/Layout";

export default function AddVehicle() {

    return (

        <Layout>

            <div className="container">

                <h1 className="page-title">
                    Add New Vehicle
                </h1>

                <div className="card">

                    <AddVehicleForm />

                </div>

            </div>

        </Layout>

    );

}