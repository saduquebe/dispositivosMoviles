// ignore_for_file: prefer_const_constructors

import 'package:enterprise_info/bloc/company_bloc.dart';
import 'package:enterprise_info/controller/company_controller.dart';
import 'package:enterprise_info/model/company.dart';
import 'package:flutter/material.dart';

class FilterCompanyPage extends StatefulWidget {
  FilterCompanyPage({Key? key}) : super(key: key);

  @override
  _FilterCompanyPageState createState() => _FilterCompanyPageState();
}

class _FilterCompanyPageState extends State<FilterCompanyPage> {
  CompanyControler controller = CompanyControler();
  String classification = "Consultoria";

  @override
  Widget build(BuildContext context) {
    String nombre = "";
    bool flag = false;
    return Scaffold(
      appBar: AppBar(
        title: Text("Busqueda de compañias"),
      ),
      body: Column(
        children: [
          TextField(
            decoration: InputDecoration(hintText: "Nombre a buscar"),
            onChanged: (value) {
              nombre = value;
            },
          ),
          TextButton(
              onPressed: () async {
                await searchByName(nombre);
                flag = true;
              },
              child: Text("Busqueda por nombre")),
          crearClase(),
          TextButton(
              onPressed: searchByClass, child: Text("Busqueda por categoria")),
          Expanded(
              child: FutureBuilder(
                  future: flag ? searchByName(nombre) : null,
                  builder: (BuildContext context,
                      AsyncSnapshot<List<Company>> companies) {
                    if (companies.hasData) {
                      print(companies.data);
                      return getCards(companies.data!);
                    }
                    return Text("Vacio");
                  })),
        ],
      ),
    );
  }

  DropdownButton crearClase() {
    List<String> items = [
      'Consultoria',
      'Desarrollo a la medida',
      'Fábrica de software',
    ];
    return DropdownButton(
        value: classification,
        items: items.map((String items) {
          return DropdownMenuItem(
            value: items,
            child: Text(items),
          );
        }).toList(),
        onChanged: (value) {
          classification = value!;
          setState(() {});
        });
  }

  ListView getCards(List<Object> data) {
    List<Company> companies = [];
    for (dynamic val in data) {
      companies.add(val);
    }
    return ListView.builder(
        itemCount: companies.length,
        itemBuilder: (context, index) {
          return Card(
            child: Row(
              children: [
                Column(children: [
                  Text(companies[index].name),
                  Text(companies[index].url),
                  Text(companies[index].phoneNumber),
                  Text(companies[index].email),
                  Text(companies[index].classification),
                  const SizedBox(
                    height: 10,
                  )
                ]),
              ],
            ),
          );
        });
  }

  Future<List<Company>> searchByName(String name) {
    Future<List<Company>> res = controller.searchByName(name);
    res.then((value) => print(value));
    return res;
  }

  void searchByClass() {}
}
