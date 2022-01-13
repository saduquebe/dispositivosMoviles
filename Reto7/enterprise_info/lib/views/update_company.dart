// ignore_for_file: prefer_const_constructors

import 'package:enterprise_info/bloc/company_bloc.dart';
import 'package:enterprise_info/bloc/provider.dart';
import 'package:enterprise_info/controller/company_controller.dart';
import 'package:enterprise_info/model/company.dart';
import 'package:flutter/material.dart';

class UpdateCompanyPage extends StatefulWidget {
  UpdateCompanyPage({Key? key}) : super(key: key);

  @override
  State<UpdateCompanyPage> createState() => _UpdateCompanyPageState();
}

class _UpdateCompanyPageState extends State<UpdateCompanyPage> {
  CompanyControler controller = CompanyControler();
  Map<String, Object> changes = {};

  @override
  Widget build(BuildContext context) {
    CompanyBloc bloc = Provider.of(context).companyBloc;
    int id = bloc.id;
    return Scaffold(
      resizeToAvoidBottomInset: false,
      appBar: AppBar(
        title: Text("Actualizar compañia"),
      ),
      body: Column(
        children: [
          crearNombre(bloc),
          crearUrl(bloc),
          crearTelefono(bloc),
          crearEmail(bloc),
          crearProductos(bloc),
          crearClase(bloc),
          crearEmpresa(id)
        ],
      ),
    );
  }

  Widget crearNombre(CompanyBloc bloc) {
    return StreamBuilder(
        stream: bloc.nameStream,
        builder: (BuildContext context, AsyncSnapshot<String> snapshot) {
          TextEditingController txt = TextEditingController();
          if (snapshot.hasData) {
            txt.text = snapshot.data!;
          }
          return Padding(
            padding: const EdgeInsets.all(10.0),
            child: TextField(
              controller: txt,
              textCapitalization: TextCapitalization.sentences,
              decoration: InputDecoration(
                border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(20.0)),
              ),
              onChanged: (value) {
                changes['name'] = value;
              },
            ),
          );
        });
  }

  Widget crearUrl(CompanyBloc bloc) {
    return StreamBuilder(
        stream: bloc.urlStream,
        builder: (BuildContext context, AsyncSnapshot<String> snapshot) {
          TextEditingController txt = TextEditingController();
          if (snapshot.hasData) {
            txt.text = snapshot.data!;
          }
          return Padding(
            padding: const EdgeInsets.all(10.0),
            child: TextField(
              controller: txt,
              keyboardType: TextInputType.url,
              textCapitalization: TextCapitalization.sentences,
              decoration: InputDecoration(
                border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(20.0)),
              ),
              onChanged: (value) {
                changes['url'] = value;
              },
            ),
          );
        });
  }

  Widget crearTelefono(CompanyBloc bloc) {
    return StreamBuilder(
        stream: bloc.phoneStream,
        builder: (BuildContext context, AsyncSnapshot<String> snapshot) {
          TextEditingController txt = TextEditingController();
          if (snapshot.hasData) {
            txt.text = snapshot.data!;
          }
          return Padding(
            padding: const EdgeInsets.all(10.0),
            child: TextField(
              controller: txt,
              keyboardType: TextInputType.number,
              textCapitalization: TextCapitalization.sentences,
              decoration: InputDecoration(
                border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(20.0)),
              ),
              onChanged: (value) {
                changes['phone'] = value;
              },
            ),
          );
        });
  }

  Widget crearEmail(CompanyBloc bloc) {
    return StreamBuilder(
        stream: bloc.emailStream,
        builder: (BuildContext context, AsyncSnapshot<String> snapshot) {
          TextEditingController txt = TextEditingController();
          if (snapshot.hasData) {
            txt.text = snapshot.data!;
          }
          return Padding(
            padding: const EdgeInsets.all(10.0),
            child: TextField(
              controller: txt,
              keyboardType: TextInputType.emailAddress,
              textCapitalization: TextCapitalization.sentences,
              decoration: InputDecoration(
                border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(20.0)),
              ),
              onChanged: (value) {
                changes['email'] = value;
              },
            ),
          );
        });
  }

  Widget crearProductos(CompanyBloc bloc) {
    return StreamBuilder(
        stream: bloc.productsStream,
        builder: (BuildContext context, AsyncSnapshot<String> snapshot) {
          TextEditingController txt = TextEditingController();
          if (snapshot.hasData) {
            txt.text = snapshot.data!;
          }
          return Padding(
            padding: const EdgeInsets.all(10.0),
            child: TextField(
              controller: txt,
              textCapitalization: TextCapitalization.sentences,
              decoration: InputDecoration(
                border: OutlineInputBorder(
                    borderRadius: BorderRadius.circular(20.0)),
              ),
              onChanged: (value) {
                changes['products'] = value;
              },
            ),
          );
        });
  }

  Widget crearClase(CompanyBloc bloc) {
    dynamic classification = "";
    List<String> items = [
      'Consultoria',
      'Desarrollo a la medida',
      'Fábrica de software',
    ];

    return StreamBuilder(
        stream: bloc.classificationStream,
        builder: (BuildContext context, AsyncSnapshot<String> snapshot) {
          return DropdownButton(
              value: snapshot.hasData ? snapshot.data! : items[0],
              items: items.map((String items) {
                return DropdownMenuItem(
                  value: items,
                  child: Text(items),
                );
              }).toList(),
              onChanged: (value) {
                changes['classification'] = value.toString();
                bloc.changeClassification(value.toString());
                setState(() {});
              });
        });
  }

  ElevatedButton crearEmpresa(int id) {
    return ElevatedButton(
        onPressed: () async {
          await controller.updateCompany(id, changes);
          Navigator.pushNamed(context, 'home');
        },
        child: Text("Actualizar datos"));
  }
}
