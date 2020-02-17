import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:atlascrm/config/ConfigSettings.dart';

import 'package:atlascrm/services/StorageService.dart';
import 'package:dio/dio.dart';
import 'package:flutter/widgets.dart';

class ApiService {
  final StorageService storageService = new StorageService();
  final String URLBASE = ConfigSettings.API_URL;
  final int TIMEOUT = 10000;

  // http.Client client;

  // ApiService() {
  //   dio = Dio(opts);
  // }

  Future<Response> publicGet(url, data) async {
    try {
      return await Dio(
        BaseOptions(
          baseUrl: ConfigSettings.API_URL,
          responseType: ResponseType.json,
          headers: {
            "Content-Type": "application/json",
          },
          sendTimeout: TIMEOUT,
        ),
      ).get(url);
    } catch (err) {
      throw err;
    }
  }

  Future<Response> publicPost(url, data) async {
    try {
      return await Dio(
        BaseOptions(
          baseUrl: ConfigSettings.API_URL,
          responseType: ResponseType.json,
          headers: {
            "Content-Type": "application/json",
          },
          sendTimeout: TIMEOUT,
        ),
      ).post(url, data: jsonEncode(data));
    } catch (err) {
      throw err;
    }
  }

  Future<Response> authGet(context, url) async {
    try {
      var token = await storageService.read("token");

      var resp = await Dio(
        BaseOptions(
          baseUrl: URLBASE + "_a1",
          responseType: ResponseType.json,
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Google $token",
          },
          sendTimeout: TIMEOUT,
        ),
      ).get(url);

      if (resp.statusCode == 401) {
        Navigator.of(context).popAndPushNamed('/logout');
        return null;
      }
      return resp;
    } catch (err) {
      if (checkAuthErrorResponse(context, err)) {
        Navigator.of(context).popAndPushNamed('/logout');
        return null;
      } else {
        throw err;
      }
    }
  }

  Future<Response> authPost(context, url, data, {isFile = false}) async {
    try {
      var token = await storageService.read("token");
      var resp = await Dio(
        BaseOptions(
          baseUrl: URLBASE + "_a1",
          responseType: ResponseType.json,
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Google $token",
          },
          sendTimeout: TIMEOUT,
        ),
      ).post(url, data: jsonEncode(data));

      if (resp.statusCode == 401) {
        Navigator.of(context).popAndPushNamed('/logout');
        return null;
      }
      return resp;
    } catch (err) {
      if (checkAuthErrorResponse(context, err)) {
        Navigator.of(context).popAndPushNamed('/logout');
        return null;
      } else {
        throw err;
      }
    }
  }

  Future<Response> authFilePost(context, url, File file) async {
    try {
      var token = await storageService.read("token");

      var f = MultipartFile.fromBytes(file.readAsBytesSync());
      var formData = FormData.fromMap({"file": f});

      var resp = await Dio(
        BaseOptions(
          baseUrl: URLBASE + "_a1",
          headers: {
            "Content-Type": "multipart/form-data",
            "Authorization": "Google $token",
          },
          sendTimeout: TIMEOUT,
        ),
      ).post(url, data: formData);

      if (resp.statusCode == 401) {
        Navigator.of(context).popAndPushNamed('/logout');
        return null;
      }
      return resp;
    } catch (err) {
      if (checkAuthErrorResponse(context, err)) {
        Navigator.of(context).popAndPushNamed('/logout');
        return null;
      } else {
        throw err;
      }
    }
  }

  Future<Response> authPut(context, url, data) async {
    try {
      var token = await storageService.read("token");

      var resp = await Dio(
        BaseOptions(
          baseUrl: URLBASE + "_a1",
          responseType: ResponseType.json,
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Google $token",
          },
          sendTimeout: TIMEOUT,
        ),
      ).put(url, data: jsonEncode(data));

      if (resp.statusCode == 401) {
        Navigator.of(context).popAndPushNamed('/logout');
        return null;
      }
      return resp;
    } catch (err) {
      if (checkAuthErrorResponse(context, err)) {
        Navigator.of(context).popAndPushNamed('/logout');
        return null;
      } else {
        throw err;
      }
    }
  }

  Future<Response> authDelete(context, url, data) async {
    try {
      var token = await storageService.read("token");

      var resp = await Dio(
        BaseOptions(
          baseUrl: URLBASE + "_a1",
          responseType: ResponseType.json,
          headers: {
            "Content-Type": "application/json",
            "Authorization": "Google $token",
          },
          sendTimeout: TIMEOUT,
        ),
      ).delete(url);

      if (resp.statusCode == 401) {
        Navigator.of(context).popAndPushNamed('/logout');
        return null;
      }
      return resp;
    } catch (err) {
      if (checkAuthErrorResponse(context, err)) {
        Navigator.of(context).popAndPushNamed('/logout');
        return null;
      } else {
        throw err;
      }
    }
  }

  bool checkAuthErrorResponse(context, msg) {
    try {
      if (msg != null) {
        if (msg.response != null) {
          if (msg.response.statusCode == 401) {
            return true;
          }
        }
      }
    } catch (err) {}
    return false;
  }
}
