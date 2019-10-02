import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { IAutoModAntiDup, defaultValue } from 'app/shared/model/bot/auto-mod-anti-dup.model';

export const ACTION_TYPES = {
  FETCH_AUTOMODANTIDUP_LIST: 'autoModAntiDup/FETCH_AUTOMODANTIDUP_LIST',
  FETCH_AUTOMODANTIDUP: 'autoModAntiDup/FETCH_AUTOMODANTIDUP',
  CREATE_AUTOMODANTIDUP: 'autoModAntiDup/CREATE_AUTOMODANTIDUP',
  UPDATE_AUTOMODANTIDUP: 'autoModAntiDup/UPDATE_AUTOMODANTIDUP',
  DELETE_AUTOMODANTIDUP: 'autoModAntiDup/DELETE_AUTOMODANTIDUP',
  RESET: 'autoModAntiDup/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAutoModAntiDup>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type AutoModAntiDupState = Readonly<typeof initialState>;

// Reducer

export default (state: AutoModAntiDupState = initialState, action): AutoModAntiDupState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_AUTOMODANTIDUP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_AUTOMODANTIDUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_AUTOMODANTIDUP):
    case REQUEST(ACTION_TYPES.UPDATE_AUTOMODANTIDUP):
    case REQUEST(ACTION_TYPES.DELETE_AUTOMODANTIDUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_AUTOMODANTIDUP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_AUTOMODANTIDUP):
    case FAILURE(ACTION_TYPES.CREATE_AUTOMODANTIDUP):
    case FAILURE(ACTION_TYPES.UPDATE_AUTOMODANTIDUP):
    case FAILURE(ACTION_TYPES.DELETE_AUTOMODANTIDUP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUTOMODANTIDUP_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUTOMODANTIDUP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_AUTOMODANTIDUP):
    case SUCCESS(ACTION_TYPES.UPDATE_AUTOMODANTIDUP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_AUTOMODANTIDUP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'services/bot/api/auto-mod-anti-dups';

// Actions

export const getEntities: ICrudGetAllAction<IAutoModAntiDup> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_AUTOMODANTIDUP_LIST,
  payload: axios.get<IAutoModAntiDup>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IAutoModAntiDup> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_AUTOMODANTIDUP,
    payload: axios.get<IAutoModAntiDup>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAutoModAntiDup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_AUTOMODANTIDUP,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAutoModAntiDup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_AUTOMODANTIDUP,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAutoModAntiDup> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_AUTOMODANTIDUP,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
